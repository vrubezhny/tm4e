package org.eclipse.textmate4e.core.internal.grammar;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.textmate4e.core.grammar.GrammarHelper;
import org.eclipse.textmate4e.core.grammar.IMatchInjectionsResult;
import org.eclipse.textmate4e.core.grammar.IMatchResult;
import org.eclipse.textmate4e.core.grammar.Injection;
import org.eclipse.textmate4e.core.grammar.StackElement;
import org.eclipse.textmate4e.core.internal.oniguruma.IOnigCaptureIndex;
import org.eclipse.textmate4e.core.internal.oniguruma.IOnigNextMatchResult;
import org.eclipse.textmate4e.core.internal.oniguruma.OnigString;
import org.eclipse.textmate4e.core.internal.rule.BeginEndRule;
import org.eclipse.textmate4e.core.internal.rule.BeginWhileRule;
import org.eclipse.textmate4e.core.internal.rule.CaptureRule;
import org.eclipse.textmate4e.core.internal.rule.ICompiledRule;
import org.eclipse.textmate4e.core.internal.rule.MatchRule;
import org.eclipse.textmate4e.core.internal.rule.Rule;

class Tokenizer {

	private final Grammar grammar;
	private final OnigString lineText;
	private boolean isFirstLine;
	private int linePos;
	private StackElement stack;
	private final LineTokens lineTokens;
	private int anchorPosition = -1;
	private boolean STOP;
	private final int lineLength;

	public Tokenizer(Grammar grammar, OnigString lineText, boolean isFirstLine, int linePos, StackElement stack,
			LineTokens lineTokens) {
		this.grammar = grammar;
		this.lineText = lineText;
		this.lineLength = lineText.utf8_length();
		this.isFirstLine = isFirstLine;
		this.linePos = linePos;
		this.stack = stack;
		this.lineTokens = lineTokens;
	}

	public StackElement scan() {
		STOP = false;

		// IWhileCheckResult whileCheckResult = _checkWhileConditions(grammar,
		// lineText, isFirstLine, linePos, stack, lineTokens);
		// stack = whileCheckResult.stack;
		// linePos = whileCheckResult.linePos;
		// isFirstLine = whileCheckResult.isFirstLine;
		// let anchorPosition = whileCheckResult.anchorPosition;
		//
		while (!STOP) {
			scanNext(); // potentially modifies linePos && anchorPosition
		}

		return stack;
	}

	private void scanNext() {
		// if (IN_DEBUG_MODE) {
		// //console.log('');
		// //console.log('@@scanNext: |' + getString(lineText).replace(/\n$/,
		// '\\n').substr(linePos) + '|');
		// }
		IMatchResult r = matchRuleOrInjections(grammar, lineText, isFirstLine, linePos, stack, anchorPosition);

		if (r == null) {
			// if (IN_DEBUG_MODE) {
			// console.log(' no more matches.');
			// }
			// No match
			lineTokens.produce(stack, lineLength);
			STOP = true;
			return;
		}

		IOnigCaptureIndex[] captureIndices = r.getCaptureIndices();
		int matchedRuleId = r.getMatchedRuleId();

		boolean hasAdvanced = (captureIndices != null && captureIndices.length > 0)
				? (captureIndices[0].getEnd() > linePos) : false;

		if (matchedRuleId == -1) {
			// We matched the `end` for this rule => pop it
			BeginEndRule poppedRule = (BeginEndRule) stack.getRule(grammar);

			// if (IN_DEBUG_MODE) {
			// console.log(' popping ' + poppedRule.debugName + ' - ' +
			// poppedRule.debugEndRegExp);
			// }

			lineTokens.produce(stack, captureIndices[0].getStart());
			stack = stack.withContentName(null);
			handleCaptures(grammar, lineText, isFirstLine, stack, lineTokens, poppedRule.endCaptures, captureIndices);
			lineTokens.produce(stack, captureIndices[0].getEnd());

			// pop
			StackElement popped = stack;
			stack = stack.pop();

			if (!hasAdvanced && popped.getEnterPos() == linePos) {
				// Grammar pushed & popped a rule without advancing
				System.err.println(
						"[1] - Grammar is in an endless loop - Grammar pushed & popped a rule without advancing");

				// See https://github.com/Microsoft/vscode-textmate/issues/12
				// Let's assume this was a mistake by the grammar author and the
				// intent was to continue in this state
				stack = stack.pushElement(popped);

				lineTokens.produce(stack, lineLength);
				STOP = true;
				return;
			}
		} else {
			// We matched a rule!
			Rule _rule = grammar.getRule(matchedRuleId);

			lineTokens.produce(stack, captureIndices[0].getStart());

			StackElement beforePush = stack;
			// push it on the stack rule
			stack = stack.push(matchedRuleId, linePos, null, _rule.getName(lineText.getString(), captureIndices),
					null);

			if (_rule instanceof BeginEndRule) {
				BeginEndRule pushedRule = (BeginEndRule) _rule;
//				if (IN_DEBUG_MODE) {
//					console.log('  pushing ' + pushedRule.debugName + ' - ' + pushedRule.debugBeginRegExp);
//				}
				
				handleCaptures(grammar, lineText, isFirstLine, stack, lineTokens, pushedRule.beginCaptures,
						captureIndices);
				lineTokens.produce(stack, captureIndices[0].getEnd());
				anchorPosition = captureIndices[0].getEnd();
				stack = stack.withContentName(pushedRule.getContentName(lineText.getString(), captureIndices));

				if (pushedRule.endHasBackReferences) {
					stack = stack.withEndRule(
							pushedRule.getEndWithResolvedBackReferences(lineText.getString(), captureIndices));
				}

				if (!hasAdvanced && beforePush.hasSameRuleAs(stack)) {
					// Grammar pushed the same rule without advancing
					System.err.println("[2] - Grammar is in an endless loop - Grammar pushed the same rule without advancing");
					stack = stack.pop();
					lineTokens.produce(stack, lineLength);
					STOP = true;
					return;
				}
			} else if (_rule instanceof BeginWhileRule) {
				BeginWhileRule pushedRule = (BeginWhileRule)_rule;
//				if (IN_DEBUG_MODE) {
//					console.log('  pushing ' + pushedRule.debugName);
//				}

				handleCaptures(grammar, lineText, isFirstLine, stack, lineTokens, pushedRule.beginCaptures, captureIndices);
				lineTokens.produce(stack, captureIndices[0].getEnd());
				anchorPosition = captureIndices[0].getEnd();
				stack = stack.withContentName(pushedRule.getContentName(lineText.getString(), captureIndices));

				if (pushedRule.whileHasBackReferences) {
					stack = stack.withEndRule(pushedRule.getWhileWithResolvedBackReferences(lineText.getString(), captureIndices));
				}

				if (!hasAdvanced && beforePush.hasSameRuleAs(stack)) {
					// Grammar pushed the same rule without advancing
					System.err.println("[3] - Grammar is in an endless loop - Grammar pushed the same rule without advancing");
					stack = stack.pop();
					lineTokens.produce(stack, lineLength);
					STOP = true;
					return;
				}
			} else {
				MatchRule matchingRule = (MatchRule)_rule;
//				if (IN_DEBUG_MODE) {
//					console.log('  matched ' + matchingRule.debugName + ' - ' + matchingRule.debugMatchRegExp);
//				}
				
				handleCaptures(grammar, lineText, isFirstLine, stack, lineTokens, matchingRule.captures, captureIndices);
				lineTokens.produce(stack, captureIndices[0].getEnd());

				// pop rule immediately since it is a MatchRule
				stack = stack.pop();
				
				if (!hasAdvanced) {
					// Grammar is not advancing, nor is it pushing/popping
					System.err.println("[4] - Grammar is in an endless loop - Grammar is not advancing, nor is it pushing/popping");
					stack = stack.safePop();
					lineTokens.produce(stack, lineLength);
					STOP = true;
					return;
				}
			}
		}
		
		if (captureIndices[0].getEnd()> linePos) {
			// Advance stream
			linePos = captureIndices[0].getEnd();
			isFirstLine = false;
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
				_tokenizeString(grammar,
						GrammarHelper.createOnigString(lineText.getString().substring(0, captureIndex.getEnd())),
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

	/**
	 * Walk the stack from bottom to top, and check each while condition in this
	 * order. If any fails, cut off the entire stack above the failed while
	 * condition. While conditions may also advance the linePosition.
	 */
	// private IWhileCheckResult _checkWhileConditions(grammar: Grammar,
	// lineText: OnigString, isFirstLine: boolean, linePos: number, stack:
	// StackElement, lineTokens: LineTokens) {
	// let anchorPosition = -1;
	// let whileRules: IWhileStack[] = [];
	// for (let node = stack; node; node = node.pop()) {
	// let nodeRule = node.getRule(grammar);
	// if (nodeRule instanceof BeginWhileRule) {
	// whileRules.push({
	// rule: nodeRule,
	// stack: node
	// });
	// }
	// }
	//
	// for (let whileRule = whileRules.pop(); whileRule; whileRule =
	// whileRules.pop()) {
	// let ruleScanner = whileRule.rule.compileWhile(grammar,
	// whileRule.stack.getEndRule(), isFirstLine, anchorPosition === linePos);
	// let r = ruleScanner.scanner._findNextMatchSync(lineText, linePos);
	// if (IN_DEBUG_MODE) {
	// console.log(' scanning for while rule');
	// console.log(debugCompiledRuleToString(ruleScanner));
	// }
	//
	// if (r) {
	// let matchedRuleId = ruleScanner.rules[r.index];
	// if (matchedRuleId != -2) {
	// // we shouldn't end up here
	// stack = whileRule.stack.pop();
	// break;
	// }
	// if (r.captureIndices && r.captureIndices.length) {
	// lineTokens.produce(whileRule.stack, r.captureIndices[0].start);
	// handleCaptures(grammar, lineText, isFirstLine, whileRule.stack,
	// lineTokens, whileRule.rule.whileCaptures, r.captureIndices);
	// lineTokens.produce(whileRule.stack, r.captureIndices[0].end);
	// anchorPosition = r.captureIndices[0].end;
	// if (r.captureIndices[0].end > linePos) {
	// linePos = r.captureIndices[0].end;
	// isFirstLine = false;
	// }
	// }
	// } else {
	// stack = whileRule.stack.pop();
	// break;
	// }
	// }
	//
	// return { stack: stack, linePos: linePos, anchorPosition: anchorPosition,
	// isFirstLine: isFirstLine };
	// }

	public static StackElement _tokenizeString(Grammar grammar, OnigString lineText, boolean isFirstLine, int linePos,
			StackElement stack, LineTokens lineTokens) {
		return new Tokenizer(grammar, lineText, isFirstLine, linePos, stack, lineTokens).scan();
	}
}
