package fr.opensagres.language.textmate.grammar;

public class Matcher<T> {

	public static <T> Matcher<T> createMatcher(String expression, matchesName: (names: string[], matcherInput: T) => boolean)  {
		var tokenizer = newTokenizer(expression);
		var token = tokenizer.next();
		return parseExpression() || (matcherInput => false);
	}

	private static <T> Matcher<T> parseOperand()  {
		if (token == '-') {
			token = tokenizer.next();
			var expressionToNegate = parseOperand();
			return matcherInput => expressionToNegate && !expressionToNegate(matcherInput);
		}
		if (token === '(') {
			token = tokenizer.next();
			var expressionInParents = parseExpression('|');
			if (token == ')') {
				token = tokenizer.next();
			}
			return expressionInParents;
		}
		if (isIdentifier(token)) {
			var identifiers : string[] = [];
			do {
				identifiers.push(token);
				token = tokenizer.next();
			} while (isIdentifier(token));
			return matcherInput => matchesName(identifiers, matcherInput);
		}
		return null;
	}
	
	private static <T> Matcher<T> parseConjunction() {
		var matchers : Matcher<T>[] = [];
		var matcher = parseOperand();
		while (matcher) {
			matchers.push(matcher);
			matcher = parseOperand();
		}
		return matcherInput => matchers.every(matcher => matcher(matcherInput)); // and
	}

	private static <T> Matcher<T> parseExpression(String orOperatorToken) {
		if (orOperatorToken == null) {
			orOperatorToken = ",";
		}
		var matchers : Matcher<T>[] = [];
		var matcher = parseConjunction();
		while (matcher) {
			matchers.push(matcher);
			if (token === orOperatorToken) {
				do {
					token = tokenizer.next();
				} while (token === orOperatorToken); // ignore subsequent commas
			} else {
				break;
			}
			matcher = parseConjunction();
		}
		return matcherInput => matchers.some(matcher => matcher(matcherInput)); // or
	}
	
	private static boolean isIdentifier(String token) {
		return token != null && token.matches("[\\w\\.:]+");
	}

	private static newTokenizer(String input) : { next: () => string } {
		let regex = /([\w\.:]+|[\,\|\-\(\)])/g;
		var match = regex.exec(input);
		return {
			next: () => {
				if (!match) {
					return null;
				}
				var res = match[0];
				match = regex.exec(input);
				return res;
			}
		}
}
