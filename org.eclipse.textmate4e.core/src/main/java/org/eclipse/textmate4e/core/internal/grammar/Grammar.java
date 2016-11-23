/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * This code is an translation of code copyrighted by Microsoft Corporation, and initially licensed under MIT.
 *
 * Contributors:
 *  - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.textmate4e.core.internal.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.textmate4e.core.grammar.IGrammar;
import org.eclipse.textmate4e.core.grammar.IGrammarRepository;
import org.eclipse.textmate4e.core.grammar.IMatchInjectionsResult;
import org.eclipse.textmate4e.core.grammar.IMatchResult;
import org.eclipse.textmate4e.core.grammar.IToken;
import org.eclipse.textmate4e.core.grammar.ITokenizeLineResult;
import org.eclipse.textmate4e.core.grammar.Injection;
import org.eclipse.textmate4e.core.grammar.StackElement;
import org.eclipse.textmate4e.core.internal.grammar.parser.Raw;
import org.eclipse.textmate4e.core.internal.oniguruma.IOnigCaptureIndex;
import org.eclipse.textmate4e.core.internal.oniguruma.IOnigNextMatchResult;
import org.eclipse.textmate4e.core.internal.oniguruma.OnigString;
import org.eclipse.textmate4e.core.internal.rule.BeginEndRule;
import org.eclipse.textmate4e.core.internal.rule.BeginWhileRule;
import org.eclipse.textmate4e.core.internal.rule.CaptureRule;
import org.eclipse.textmate4e.core.internal.rule.ICompiledRule;
import org.eclipse.textmate4e.core.internal.rule.IRuleFactory;
import org.eclipse.textmate4e.core.internal.rule.IRuleFactoryHelper;
import org.eclipse.textmate4e.core.internal.rule.MatchRule;
import org.eclipse.textmate4e.core.internal.rule.Rule;
import org.eclipse.textmate4e.core.internal.rule.RuleFactory;
import org.eclipse.textmate4e.core.internal.types.IRawGrammar;
import org.eclipse.textmate4e.core.internal.types.IRawRepository;
import org.eclipse.textmate4e.core.internal.types.IRawRule;

/**
 * TextMate grammar implementation.
 * 
 * @see https://github.com/Microsoft/vscode-textmate/blob/master/src/grammar.ts
 *
 */
public class Grammar implements IGrammar, IRuleFactoryHelper {

	private int _rootId;
	private int _lastRuleId;
	private final Map<Integer, Rule> _ruleId2desc;
	private final Map<String, IRawGrammar> _includedGrammars;
	private final IGrammarRepository _grammarRepository;
	private final IRawGrammar _grammar;
	private List<Injection> _injections;

	public Grammar(IRawGrammar grammar, IGrammarRepository grammarRepository) {
		this._rootId = -1;
		this._lastRuleId = 0;
		this._includedGrammars = new HashMap<String, IRawGrammar>();
		this._grammarRepository = grammarRepository;
		this._grammar = initGrammar(grammar, null);
		this._ruleId2desc = new HashMap<Integer, Rule>();
		this._injections = new ArrayList<Injection>();
	}

	public List<Injection> getInjections(StackElement states) {
		if (this._injections == null) {
			this._injections = getGrammarInjections(this._grammar, this);
			// optional: bring in injections from external repositories
		}
		if (this._injections.size() == 0) {
			return this._injections;
		}
		return filter(this._injections, states);
	}

	private List<Injection> filter(List<Injection> injections, StackElement states) {
		List<Injection> filtered = new ArrayList<Injection>();
		for (Injection injection : injections) {
			if (injection.match(states)) {
				filtered.add(injection);
			}
		}
		return filtered;
	}

	public Rule registerRule(IRuleFactory factory) {
		int id = (++this._lastRuleId);
		Rule result = factory.create(id);
		this._ruleId2desc.put(id, result);
		return result;
	}

	public Rule getRule(int patternId) {
		return this._ruleId2desc.get(patternId);
	}

	@Override
	public IRawGrammar getExternalGrammar(String scopeName, IRawRepository repository) {
		if (this._includedGrammars.containsKey(scopeName)) {
			return this._includedGrammars.get(scopeName);
		} else if (this._grammarRepository != null) {
			IRawGrammar rawIncludedGrammar = this._grammarRepository.lookup(scopeName);
			if (rawIncludedGrammar != null) {
				this._includedGrammars.put(scopeName, initGrammar(rawIncludedGrammar, repository.getBase()));
				return this._includedGrammars.get(scopeName);
			}
		}
		return null;
	}

	private IRawGrammar initGrammar(IRawGrammar grammar, IRawRule base) {
		grammar = clone(grammar);
		if (grammar.getRepository() != null) {
			Raw self = new Raw();
			self.setPatterns(grammar.getPatterns());
			self.setName(grammar.getScopeName());
			grammar.getRepository().setSelf(self);
		}
		if (base != null) {
			grammar.getRepository().setBase(base);
		} else {
			grammar.getRepository().setBase(grammar.getRepository().getSelf());
		}
		return grammar;
	}

	private IRawGrammar clone(IRawGrammar grammar) {
		return (IRawGrammar) ((Raw) grammar).clone();
	}

	private void handleCaptures(Grammar grammar, OnigString lineText, boolean isFirstLine, StackElement stack,
			LineTokens lineTokens, List<CaptureRule> captures, IOnigCaptureIndex[] captureIndices) {
		if (captures.size() == 0) {
			return;
		}

		int len = Math.min(captures.size(), captureIndices.length);
		List<LocalStackElement> localStack = new ArrayList<LocalStackElement>();
		int maxEnd = captureIndices[0].getEnd();
		IOnigCaptureIndex captureIndex;

		for (int i = 0; i < len; i++) {
			CaptureRule captureRule = captures.get(i);
			if (captureRule == null) {
				// Not interested
				continue;
			}

			captureIndex = captureIndices[i];

			if (captureIndex.getLength() == 0) {
				// Nothing really captured
				continue;
			}

			if (captureIndex.getStart() > maxEnd) {
				// Capture going beyond consumed string
				break;
			}

			// pop captures while needed
			while (localStack.size() > 0
					&& localStack.get(localStack.size() - 1).getEndPos() <= captureIndex.getStart()) {
				// pop!
				lineTokens.produce(stack, localStack.get(localStack.size() - 1).getEndPos(), localStack);
				localStack.remove(localStack.size() - 1);
			}

			lineTokens.produce(stack, captureIndex.getStart(), localStack);

			if (captureRule.retokenizeCapturedWithRuleId != null) {
				// the capture requires additional matching
				StackElement stackClone = stack.push(captureRule.retokenizeCapturedWithRuleId, captureIndex.getStart(),
						null, captureRule.getName(lineText.getString(), captureIndices),
						captureRule.getContentName(lineText.getString(), captureIndices));
				_tokenizeString(grammar, createOnigString(lineText.getString().substring(0, captureIndex.getEnd())),
						(isFirstLine && captureIndex.getStart() == 0), captureIndex.getStart(), stackClone, lineTokens);
				continue;
			}

			// push
			localStack.add(new LocalStackElement(captureRule.getName(lineText.getString(), captureIndices),
					captureIndex.getEnd()));
		}

		while (localStack.size() > 0) {
			// pop!
			lineTokens.produce(stack, localStack.get(localStack.size() - 1).getEndPos(), localStack);
			localStack.remove(localStack.size() - 1);
		}
	}

	private OnigString createOnigString(String str) {
		return new OnigString(str);
	}

	private List<Injection> getGrammarInjections(IRawGrammar grammar, IRuleFactoryHelper ruleFactoryHelper) {
		List<Injection> injections = new ArrayList<Injection>();
		// var rawInjections = grammar.injections;
		// if (rawInjections) {
		// var nameMatcher = (identifers: string[], stackElements:
		// StackElement[]) => {
		// var lastIndex = 0;
		// return identifers.every(identifier => {
		// for (var i = lastIndex; i < stackElements.length; i++) {
		// if (stackElements[i].matches(identifier)) {
		// lastIndex = i;
		// return true;
		// }
		// }
		// return false;
		// });
		// };
		//
		// for (var expression in rawInjections) {
		// var subExpressions = (<string> expression).split(',');
		// subExpressions.forEach(subExpression => {
		// var expressionString = subExpression.replace(/L:/g, '')
		//
		// injections.push({
		// matcher: createMatcher(expressionString, nameMatcher),
		// ruleId: RuleFactory.getCompiledRuleId(rawInjections[expression],
		// ruleFactoryHelper, grammar.repository),
		// grammar: grammar,
		// priorityMatch: expressionString.length < subExpression.length
		// });
		// });
		// }
		// }
		return injections;
	}

	@Override
	public ITokenizeLineResult tokenizeLine(String lineText) {
		return tokenizeLine(lineText, null);
	}

	@Override
	public ITokenizeLineResult tokenizeLine(String lineText, StackElement prevState) {
		if (this._rootId == -1) {
			this._rootId = RuleFactory.getCompiledRuleId(this._grammar.getRepository().getSelf(), this,
					this._grammar.getRepository());
		}

		boolean isFirstLine;
		if (prevState == null) {
			isFirstLine = true;
			prevState = new StackElement(null, this._rootId, -1, null, this.getRule(this._rootId).getName(null, null),
					null);
		} else {
			isFirstLine = false;
			prevState.reset();
		}

		lineText = lineText + '\n';
		OnigString onigLineText = createOnigString(lineText);
		int lineLength = lineText.length();
		LineTokens lineTokens = new LineTokens();
		StackElement nextState = _tokenizeString(this, onigLineText, isFirstLine, 0, prevState, lineTokens);

		IToken[] _produced = lineTokens.getResult(nextState, lineLength);

		return new TokenizeLineResult(_produced, nextState);
	}

	private class ScanContext {

		public final Grammar grammar;
		public final OnigString lineText;
		public boolean isFirstLine;
		public int linePos;
		public StackElement stack;
		public final LineTokens lineTokens;
		public int anchorPosition = -1;

		public ScanContext(Grammar grammar, OnigString lineText, boolean isFirstLine, int linePos, StackElement stack,
				LineTokens lineTokens) {
			this.grammar = grammar;
			this.lineText = lineText;
			this.isFirstLine = isFirstLine;
			this.linePos = linePos;
			this.stack = stack;
			this.lineTokens = lineTokens;
		}
	}

	private IMatchResult matchRule(Grammar grammar, OnigString lineText, boolean isFirstLine, final int linePos,
			StackElement stack, int anchorPosition) {
		Rule rule = stack.getRule(grammar);
		final ICompiledRule ruleScanner = rule.compile(grammar, stack.getEndRule(), isFirstLine,
				linePos == anchorPosition);
		final IOnigNextMatchResult r = ruleScanner.scanner._findNextMatchSync(lineText, linePos);

		if (r != null) {
			return new IMatchResult() {

				@Override
				public int getMatchedRuleId() {
					return ruleScanner.rules[r.getIndex()];
				}

				@Override
				public IOnigCaptureIndex[] getCaptureIndices() {
					return r.getCaptureIndices();
				}
			};
		}
		return null;
	}

	private IMatchResult matchRuleOrInjections(Grammar grammar, OnigString lineText, boolean isFirstLine,
			final int linePos, StackElement stack, int anchorPosition) {
		// Look for normal grammar rule
		IMatchResult matchResult = matchRule(grammar, lineText, isFirstLine, linePos, stack, anchorPosition);

		// Look for injected rules
		List<Injection> injections = grammar.getInjections(stack);
		if (injections.size() == 0) {
			// No injections whatsoever => early return
			return matchResult;
		}

		IMatchInjectionsResult injectionResult = matchInjections(injections, grammar, lineText, isFirstLine, linePos,
				stack, anchorPosition);
		if (injectionResult == null) {
			// No injections matched => early return
			return matchResult;
		}

		if (matchResult == null) {
			// Only injections matched => early return
			return injectionResult;
		}

		// Decide if `matchResult` or `injectionResult` should win
		int matchResultScore = matchResult.getCaptureIndices()[0].getStart();
		int injectionResultScore = injectionResult.getCaptureIndices()[0].getStart();

		if (injectionResultScore < matchResultScore
				|| (injectionResult.isPriorityMatch() && injectionResultScore == matchResultScore)) {
			// injection won!
			return injectionResult;
		}

		return matchResult;
	}

	private IMatchInjectionsResult matchInjections(List<Injection> injections, Grammar grammar, OnigString lineText,
			boolean isFirstLine, int linePos, StackElement stack, int anchorPosition) {
		// The lower the better
		int bestMatchRating = Integer.MAX_VALUE;
		IOnigCaptureIndex[] bestMatchCaptureIndices = null;
		int bestMatchRuleId = -1;
		boolean bestMatchResultPriority = false;

		for (Injection injection : injections) {
			ICompiledRule ruleScanner = grammar.getRule(injection.ruleId).compile(grammar, null, isFirstLine,
					linePos == anchorPosition);
			IOnigNextMatchResult matchResult = ruleScanner.scanner._findNextMatchSync(lineText, linePos);

			if (matchResult == null) {
				continue;
			}

			int matchRating = matchResult.getCaptureIndices()[0].getStart();

			if (matchRating >= bestMatchRating) {
				continue;
			}

			bestMatchRating = matchRating;
			bestMatchCaptureIndices = matchResult.getCaptureIndices();
			bestMatchRuleId = ruleScanner.rules[matchResult.getIndex()];
			bestMatchResultPriority = injection.priorityMatch;

			if (bestMatchRating == linePos && bestMatchResultPriority) {
				// No more need to look at the rest of the injections
				break;
			}
		}

		if (bestMatchCaptureIndices != null) {
			final int matchedRuleId = bestMatchRuleId;
			final IOnigCaptureIndex[] matchCaptureIndices = bestMatchCaptureIndices;
			final boolean matchResultPriority = bestMatchResultPriority;
			return new IMatchInjectionsResult() {

				@Override
				public int getMatchedRuleId() {
					return matchedRuleId;
				}

				@Override
				public IOnigCaptureIndex[] getCaptureIndices() {
					return matchCaptureIndices;
				}

				@Override
				public boolean isPriorityMatch() {
					return matchResultPriority;
				}
			};
		}

		return null;
	}

	private StackElement _tokenizeString(Grammar grammar, OnigString lineText, boolean isFirstLine, int linePos,
			StackElement stack, LineTokens lineTokens) {
		int lineLength = lineText.utf8_length();
		ScanContext ctx = new ScanContext(grammar, lineText, isFirstLine, linePos, stack, lineTokens);
		while (ctx.linePos < lineLength) {
			// System.err.println(ctx.linePos + "/" + lineLength);
			scanNext(ctx); // potentially modifies linePos && anchorPosition
		}
		return ctx.stack;
	}

	private boolean scanNext(ScanContext ctx) {
		StackElement stack = ctx.stack;
		Grammar grammar = ctx.grammar;
		OnigString lineText = ctx.lineText;
		LineTokens lineTokens = ctx.lineTokens;
		int lineLength = lineText.utf8_length(); // getString(lineText).length;

		IMatchResult r = matchRuleOrInjections(grammar, lineText, ctx.isFirstLine, ctx.linePos, stack,
				ctx.anchorPosition);

		if (r == null) {
			// No match
			lineTokens.produce(stack, lineLength);
			ctx.linePos = lineLength;
			return true;
		}

		IOnigCaptureIndex[] captureIndices = r.getCaptureIndices();
		int matchedRuleId = r.getMatchedRuleId();

		boolean hasAdvanced = (captureIndices[0].getEnd() > ctx.linePos);

		if (matchedRuleId == -1) {
			// We matched the `end` for this rule => pop it
			BeginEndRule poppedRule = (BeginEndRule) stack.getRule(grammar);

			lineTokens.produce(stack, captureIndices[0].getStart());
			stack = stack.withContentName(null);
			handleCaptures(grammar, lineText, ctx.isFirstLine, stack, lineTokens, poppedRule.endCaptures,
					captureIndices);
			lineTokens.produce(stack, captureIndices[0].getEnd());

			// pop
			StackElement popped = stack;
			stack = stack.pop();

			if (!hasAdvanced && popped.getEnterPos() == ctx.linePos) {
				// Grammar pushed & popped a rule without advancing
				System.err.println("Grammar is in an endless loop - case 1");

				// See https://github.com/Microsoft/vscode-textmate/issues/12
				// Let's assume this was a mistake by the grammar author and the
				// intent was to continue in this state
				stack = stack.pushElement(popped);

				lineTokens.produce(stack, lineLength);
				ctx.linePos = lineLength;
				ctx.stack = stack;
				return false;
			}

		} else {
			// We matched a rule!
			Rule _rule = grammar.getRule(matchedRuleId);

			lineTokens.produce(stack, captureIndices[0].getStart());

			StackElement beforePush = stack;
			// push it on the stack rule
			stack = stack.push(matchedRuleId, ctx.linePos, null, _rule.getName(lineText.getString(), captureIndices),
					null);

			if (_rule instanceof BeginEndRule) {
				BeginEndRule pushedRule = (BeginEndRule) _rule;

				handleCaptures(grammar, lineText, ctx.isFirstLine, stack, lineTokens, pushedRule.beginCaptures,
						captureIndices);
				lineTokens.produce(stack, captureIndices[0].getEnd());
				ctx.anchorPosition = captureIndices[0].getEnd();
				stack = stack.withContentName(pushedRule.getContentName(lineText.getString(), captureIndices));

				if (pushedRule.endHasBackReferences) {
					stack = stack.withEndRule(
							pushedRule.getEndWithResolvedBackReferences(lineText.getString(), captureIndices));
				}

				if (!hasAdvanced && beforePush.hasSameRuleAs(stack)) {
					// Grammar pushed the same rule without advancing
					System.err.println("Grammar is in an endless loop - case 2");
					stack = stack.pop();
					lineTokens.produce(stack, lineLength);
					ctx.linePos = lineLength;
					ctx.stack = stack;
					return false;
				}
			} else if (_rule instanceof BeginWhileRule) {
				BeginWhileRule pushedRule = (BeginWhileRule) _rule;
				// if (IN_DEBUG_MODE) {
				// console.log(' pushing ' + pushedRule.debugName);
				// }

				handleCaptures(grammar, lineText, ctx.isFirstLine, stack, lineTokens, pushedRule.beginCaptures,
						captureIndices);
				lineTokens.produce(stack, captureIndices[0].getEnd());
				ctx.anchorPosition = captureIndices[0].getEnd();
				stack = stack.withContentName(pushedRule.getContentName(lineText.getString(), captureIndices));

				if (pushedRule.whileHasBackReferences) {
					stack = stack.withEndRule(
							pushedRule.getWhileWithResolvedBackReferences(lineText.getString(), captureIndices));
				}

				if (!hasAdvanced && beforePush.hasSameRuleAs(stack)) {
					// Grammar pushed the same rule without advancing
					System.err.println(
							"[3] - Grammar is in an endless loop - Grammar pushed the same rule without advancing");
					stack = stack.pop();
					lineTokens.produce(stack, lineLength);
					// //STOP = true;
					ctx.linePos = lineLength;
					ctx.stack = stack;
					return false;
				}
			} else {
				MatchRule matchingRule = (MatchRule) _rule;

				handleCaptures(grammar, lineText, ctx.isFirstLine, stack, lineTokens, matchingRule.captures,
						captureIndices);
				lineTokens.produce(stack, captureIndices[0].getEnd());

				// pop rule immediately since it is a MatchRule
				stack = stack.pop();

				if (!hasAdvanced) {
					// Grammar is not advancing, nor is it pushing/popping
					System.err.println("Grammar is in an endless loop - case 3");
					stack = stack.safePop();
					lineTokens.produce(stack, lineLength);
					ctx.linePos = lineLength;
					ctx.stack = stack;
					return false;
				}
			}
		}
		ctx.stack = stack;
		if (captureIndices[0].getEnd() > ctx.linePos) {
			// Advance stream
			ctx.linePos = captureIndices[0].getEnd();
			ctx.isFirstLine = false;
		}
		return true;
	}

}
