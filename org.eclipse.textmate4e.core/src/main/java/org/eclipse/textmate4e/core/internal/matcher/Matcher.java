package org.eclipse.textmate4e.core.internal.matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.textmate4e.core.grammar.StackElement;

public class Matcher<T> implements IMatcher<T> {

	public static IMatcher<StackElement> createMatcher(String expression) {
		return createMatcher(expression, IMatchesName.NAME_MATCHER);
	}

	public static <T> IMatcher<T> createMatcher(String expression, IMatchesName<T> matchesName) {
		return new Matcher<T>(expression, matchesName);
	}

	private final Tokenizer tokenizer;
	private final IMatchesName<T> matchesName;
	private String token;
	private IMatcher<T> matcherRoot;

	public Matcher(String expression, IMatchesName<T> matchesName) {
		this.tokenizer = newTokenizer(expression);
		this.token = tokenizer.next();
		this.matchesName = matchesName;
		this.matcherRoot = parseExpression();
	}

	private IMatcher<T> parseExpression() {
		return parseExpression(",");
	}

	private IMatcher<T> parseExpression(String orOperatorToken) {
		List<IMatcher<T>> matchers = new ArrayList<>();
		IMatcher<T> matcher = parseConjunction();
		while (matcher != null) {
			matchers.add(matcher);
			if (orOperatorToken.equals(token)) {
				do {
					token = tokenizer.next();
				} while (orOperatorToken.equals(token)); // ignore subsequent
															// commas
			} else {
				break;
			}
			matcher = parseConjunction();
		}
		// some (or)
		return new IMatcher<T>() {
			@Override
			public boolean match(T matcherInput) {
				for (IMatcher<T> matcher : matchers) {
					if (matcher.match(matcherInput)) {
						return true;
					}
				}
				return false;
			}
		};
	}

	private IMatcher<T> parseConjunction() {
		List<IMatcher<T>> matchers = new ArrayList<>();
		IMatcher<T> matcher = parseOperand();
		while (matcher != null) {
			matchers.add(matcher);
			matcher = parseOperand();
		}
		// every (and)
		return new IMatcher<T>() {
			@Override
			public boolean match(T matcherInput) {
				for (IMatcher<T> matcher : matchers) {
					if (!matcher.match(matcherInput)) {
						return false;
					}
				}
				return true;
			}
		};
	}

	private IMatcher<T> parseOperand() {
		if ("-".equals(token)) {
			token = tokenizer.next();
			IMatcher<T> expressionToNegate = parseOperand();
			// return matcherInput => expressionToNegate &&
			// !expressionToNegate(matcherInput);
			return new IMatcher<T>() {
				@Override
				public boolean match(T matcherInput) {
					if (expressionToNegate == null) {
						return false;
					}
					return !expressionToNegate.match(matcherInput);
				}
			};
		}
		if ("(".equals(token)) {
			token = tokenizer.next();
			IMatcher<T> expressionInParents = parseExpression("|");
			if (")".equals(token)) {
				token = tokenizer.next();
			}
			return expressionInParents;
		}
		if (isIdentifier(token)) {
			Collection<String> identifiers = new ArrayList<>();
			do {
				identifiers.add(token);
				token = tokenizer.next();
			} while (isIdentifier(token));
			// return matcherInput => matchesName(identifiers, matcherInput);
			return new IMatcher<T>() {
				@Override
				public boolean match(T matcherInput) {
					return Matcher.this.matchesName.match(identifiers, matcherInput);
				}
			};
		}
		return null;
	}

	private boolean isIdentifier(String token) {
		return token != null && token.matches("[\\w\\.:]+");
	}

	@Override
	public boolean match(T matcherInput) {
		if (matcherRoot != null) {
			return matcherRoot.match(matcherInput);
		}
		return false;
	}

	private static class Tokenizer {

		private static final Pattern REGEXP = Pattern.compile("([\\w\\.:]+|[\\,\\|\\-\\(\\)])");

		private java.util.regex.Matcher regex;

		public Tokenizer(String input) {
			this.regex = REGEXP.matcher(input);
		}

		public String next() {
			if (regex.find()) {
				return regex.group();
			}
			return null;
		}

	}

	private static Tokenizer newTokenizer(String input) {
		return new Tokenizer(input);
	}

}