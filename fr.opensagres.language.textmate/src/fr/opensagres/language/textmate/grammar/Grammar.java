package fr.opensagres.language.textmate.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.language.textmate.grammar.parser.Raw;
import fr.opensagres.language.textmate.rule.IRuleFactory;
import fr.opensagres.language.textmate.rule.IRuleFactoryHelper;
import fr.opensagres.language.textmate.rule.Rule;
import fr.opensagres.language.textmate.rule.RuleFactory;
import fr.opensagres.language.textmate.types.IRawGrammar;
import fr.opensagres.language.textmate.types.IRawRepository;
import fr.opensagres.language.textmate.types.IRawRule;

public class Grammar implements IGrammar, IRuleFactoryHelper {

	private final Map<String, IRawGrammar> _includedGrammars;
	private final IRawGrammar _grammar;
	private final IGrammarRepository _grammarRepository;

	private int _rootId;
	private int _lastRuleId;
	private final Map<Integer, Rule> _ruleId2desc;

	public Grammar(IRawGrammar grammar, IGrammarRepository grammarRepository) {
		this._rootId = -1;
		this._lastRuleId = 0;
		this._includedGrammars = new HashMap<String, IRawGrammar>();
		this._grammarRepository = grammarRepository;
		this._grammar = initGrammar(grammar, null);
		this._ruleId2desc = new HashMap<Integer, Rule>();
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
		// grammar.repository = grammar.repository || <any>{};
		// grammar.repository.$self = { patterns: grammar.patterns, name:
		// grammar.scopeName }; grammar.repository.$base = base ||
		// grammar.repository.$self;

		return grammar;
	}

	private IRawGrammar clone(IRawGrammar grammar) {

		return grammar;
	}

	private void _tokenizeString(Grammar grammar, /* OnigString */ String lineText, boolean isFirstLine, int linePos,
			List<StackElement> stack, LineTokens lineTokens) {
		int lineLength = lineText.length(); // getString(lineText).length;
		int anchorPosition = -1;

		// while (linePos < lineLength) {
		// //scanNext(); // potentially modifies linePos && anchorPosition
		// }

	}
}
