package fr.opensagres.language.textmate.grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.language.textmate.grammar.parser.Raw;
import fr.opensagres.language.textmate.oniguruma.IOnigCaptureIndex;
import fr.opensagres.language.textmate.oniguruma.IOnigNextMatchResult;
import fr.opensagres.language.textmate.rule.BeginEndRule;
import fr.opensagres.language.textmate.rule.CaptureRule;
import fr.opensagres.language.textmate.rule.ICompiledRule;
import fr.opensagres.language.textmate.rule.IRuleFactory;
import fr.opensagres.language.textmate.rule.IRuleFactoryHelper;
import fr.opensagres.language.textmate.rule.MatchRule;
import fr.opensagres.language.textmate.rule.Rule;
import fr.opensagres.language.textmate.rule.RuleFactory;
import fr.opensagres.language.textmate.types.IRawGrammar;
import fr.opensagres.language.textmate.types.IRawRepository;
import fr.opensagres.language.textmate.types.IRawRule;

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

	public List<Injection> getInjections(List<StackElement> states) {
		if (this._injections == null) {
			this._injections = getGrammarInjections(this._grammar, this);
			// optional: bring in injections from external repositories
		}
		if (this._injections.size() == 0) {
			return this._injections;
		}
		return filter(this._injections, states);
	}

	private List<Injection> filter(List<Injection> injections, List<StackElement> states) {
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

	private void handleCaptures(Grammar grammar, String lineText /*: OnigString*/, boolean isFirstLine, List<StackElement> stack, LineTokens lineTokens, Collection<CaptureRule> captures, IOnigCaptureIndex[] captureIndices){
		if (captures.size() == 0) {
			return;
		}

		let len = Math.min(captures.length, captureIndices.length),
			localStack: LocalStackElement[] = [],
			maxEnd = captureIndices[0].end,
			i: number,
			captureRule: CaptureRule,
			captureIndex: IOnigCaptureIndex;

		for (i = 0; i < len; i++) {
			captureRule = captures[i];
			if (captureRule === null) {
				// Not interested
				continue;
			}

			captureIndex = captureIndices[i];

			if (captureIndex.length === 0) {
				// Nothing really captured
				continue;
			}

			if (captureIndex.start > maxEnd) {
				// Capture going beyond consumed string
				break;
			}

			// pop captures while needed
			while (localStack.length > 0 && localStack[localStack.length - 1].endPos <= captureIndex.start) {
				// pop!
				lineTokens.produce(stack, localStack[localStack.length - 1].endPos, localStack);
				localStack.pop();
			}

			lineTokens.produce(stack, captureIndex.start, localStack);

			if (captureRule.retokenizeCapturedWithRuleId) {
				// the capture requires additional matching
				let stackClone = stack.map((el) => el.clone());
				stackClone.push(new StackElement(captureRule.retokenizeCapturedWithRuleId, captureIndex.start, null, captureRule.getName(getString(lineText), captureIndices), captureRule.getContentName(getString(lineText), captureIndices)))
				_tokenizeString(grammar,
					createOnigString(
						getString(lineText).substring(0, captureIndex.end)
					),
					(isFirstLine && captureIndex.start === 0), captureIndex.start, stackClone, lineTokens
				);
				continue;
			}

			// push
			localStack.push(new LocalStackElement(captureRule.getName(getString(lineText), captureIndices), captureIndex.end));
		}

		while (localStack.length > 0) {
			// pop!
			lineTokens.produce(stack, localStack[localStack.length - 1].endPos, localStack);
			localStack.pop();
		}
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
	public ITokenizeLineResult tokenizeLine(String lineText, List<StackElement> prevState) {
		if (this._rootId == -1) {
			this._rootId = RuleFactory.getCompiledRuleId(this._grammar.getRepository().getSelf(), this,
					this._grammar.getRepository());
		}

		boolean isFirstLine;
		if (prevState == null) {
			isFirstLine = true;
			prevState = new ArrayList<StackElement>();
			prevState.add(
					new StackElement(this._rootId, -1, null, this.getRule(this._rootId).getName(null, null), null));
		} else {
			isFirstLine = false;
			for (StackElement state : prevState) {
				state.setEnterPos(-1);
			}
		}

		lineText = lineText + '\n';
		// let onigLineText = Rule.createOnigString(lineText);
		// let lineLength = getString(onigLineText).length;
		int lineLength = lineText.length();
		LineTokens lineTokens = new LineTokens();
		// _tokenizeString(this, onigLineText, isFirstLine, 0, prevState,
		// lineTokens);
		_tokenizeString(this, lineText, isFirstLine, 0, prevState, lineTokens);

		IToken[] _produced = lineTokens.getResult(prevState, lineLength);

		return new TokenizeLineResult(_produced, prevState);
	}

	private class ScanContext {

		public final Grammar grammar;
		public final /* OnigString */ String lineText;
		public final boolean isFirstLine;
		public final int linePos;
		public final List<StackElement> stack;
		public final LineTokens lineTokens;
		public int anchorPosition = -1;

		public ScanContext(Grammar grammar, /* OnigString */ String lineText, boolean isFirstLine, int linePos,
				List<StackElement> stack, LineTokens lineTokens) {
			this.grammar = grammar;
			this.lineText = lineText;
			this.isFirstLine = isFirstLine;
			this.linePos = linePos;
			this.stack = stack;
			this.lineTokens = lineTokens;
		}
	}

	private IMatchResult matchRule(Grammar grammar, String lineText, boolean isFirstLine, int linePos,
			List<StackElement> stack, int anchorPosition) {
		StackElement stackElement = stack.get(stack.size() - 1);
		final ICompiledRule ruleScanner = grammar.getRule(stackElement.getRuleId()).compile(grammar,
				stackElement.getEndRule(), isFirstLine, linePos == anchorPosition);
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

	private IMatchResult matchRuleOrInjections(Grammar grammar, String lineText, boolean isFirstLine, int linePos,
			List<StackElement> stack, int anchorPosition) {
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

	private IMatchInjectionsResult matchInjections(List<Injection> injections, Grammar grammar, String lineText,
			boolean isFirstLine, int linePos, List<StackElement> stack, int anchorPosition) {
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

	private void _tokenizeString(Grammar grammar, /* OnigString */ String lineText, boolean isFirstLine, int linePos,
			List<StackElement> stack, LineTokens lineTokens) {
		int lineLength = lineText.length(); // getString(lineText).length;

		ScanContext ctx = new ScanContext(grammar, lineText, isFirstLine, linePos, stack, lineTokens);
		while (linePos < lineLength) {
			scanNext(ctx); // potentially modifies linePos && anchorPosition
		}
	}

	private boolean scanNext(ScanContext ctx) {
		List<StackElement> stack = ctx.stack;
		StackElement stackElement = stack.get(stack.size() - 1);
		Grammar grammar = ctx.grammar;
		String lineText = ctx.lineText;
		boolean isFirstLine = ctx.isFirstLine;
		int linePos = ctx.linePos;
		LineTokens lineTokens = ctx.lineTokens;
		int lineLength = lineText.length(); // getString(lineText).length;

		IMatchResult r = matchRuleOrInjections(grammar, lineText, isFirstLine, linePos, stack, ctx.anchorPosition);

		if (r == null) {
			// No match
			lineTokens.produce(stack, lineLength);
			linePos = lineLength;
			return true;
		}

		IOnigCaptureIndex[] captureIndices = r.getCaptureIndices();
		int matchedRuleId = r.getMatchedRuleId();

		boolean hasAdvanced = (captureIndices[0].getEnd() > linePos);

		if (matchedRuleId == -1) {
			// We matched the `end` for this rule => pop it
			BeginEndRule poppedRule = (BeginEndRule) grammar.getRule(stackElement.getRuleId());

			lineTokens.produce(stack, captureIndices[0].getStart());
			stackElement.setContentName(null);
			handleCaptures(grammar, lineText, isFirstLine, stack, lineTokens, poppedRule.endCaptures, captureIndices);
			lineTokens.produce(stack, captureIndices[0].getEnd());

			// pop
			stack.remove(stack.size() - 1); // stack.pop();

			if (!hasAdvanced && stackElement.getEnterPos() == linePos) {
				// Grammar pushed & popped a rule without advancing
				System.err.println("Grammar is in an endless loop - case 1");
				lineTokens.produce(stack, lineLength);
				linePos = lineLength;
				return false;
			}

		} else {
			// We matched a rule!
			Rule _rule = grammar.getRule(matchedRuleId);

			lineTokens.produce(stack, captureIndices[0].getStart());

			// push it on the stack rule
			stack.add(new StackElement(matchedRuleId, linePos, null, _rule.getName(getString(lineText), captureIndices),
					null));

			if (_rule instanceof BeginEndRule) {
				BeginEndRule pushedRule = (BeginEndRule) _rule;

				handleCaptures(grammar, lineText, isFirstLine, stack, lineTokens, pushedRule.beginCaptures,
						captureIndices);
				lineTokens.produce(stack, captureIndices[0].getEnd());
				ctx.anchorPosition = captureIndices[0].getEnd();
				stack.get(stack.size() - 1)
						.setContentName(pushedRule.getContentName(getString(lineText), captureIndices));

				if (pushedRule.endHasBackReferences) {
					stack.get(stack.size() - 1).setEndRule(
							pushedRule.getEndWithResolvedBackReferences(getString(lineText), captureIndices));
				}

				if (!hasAdvanced && stackElement.getRuleId() == stack.get(stack.size() - 1).getRuleId()) {
					// Grammar pushed the same rule without advancing
					System.err.println("Grammar is in an endless loop - case 2");
					stack.remove(stack.size() - 1);
					lineTokens.produce(stack, lineLength);
					linePos = lineLength;
					return false;
				}
			} else {
				MatchRule matchingRule = (MatchRule) _rule;

				handleCaptures(grammar, lineText, isFirstLine, stack, lineTokens, matchingRule.captures,
						captureIndices);
				lineTokens.produce(stack, captureIndices[0].getEnd());

				// pop rule immediately since it is a MatchRule
				stack.remove(stack.size() - 1);

				if (!hasAdvanced) {
					// Grammar is not advancing, nor is it pushing/popping
					System.err.println("Grammar is in an endless loop - case 3");
					if (stack.size() > 1) {
						stack.remove(stack.size() - 1);
					}
					lineTokens.produce(stack, lineLength);
					linePos = lineLength;
					return false;
				}
			}
		}

		if (captureIndices[0].getEnd() > linePos) {
			// Advance stream
			linePos = captureIndices[0].getEnd();
			isFirstLine = false;
		}
		return true;
	}

	private String getString(String lineText) {
		// TODO Auto-generated method stub
		return lineText;
	}
}
