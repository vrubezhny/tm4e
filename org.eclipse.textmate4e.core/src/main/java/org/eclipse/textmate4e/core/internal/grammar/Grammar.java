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

import org.eclipse.textmate4e.core.grammar.GrammarHelper;
import org.eclipse.textmate4e.core.grammar.IGrammar;
import org.eclipse.textmate4e.core.grammar.IGrammarRepository;
import org.eclipse.textmate4e.core.grammar.IToken;
import org.eclipse.textmate4e.core.grammar.ITokenizeLineResult;
import org.eclipse.textmate4e.core.grammar.Injection;
import org.eclipse.textmate4e.core.grammar.StackElement;
import org.eclipse.textmate4e.core.internal.grammar.parser.Raw;
import org.eclipse.textmate4e.core.internal.oniguruma.OnigString;
import org.eclipse.textmate4e.core.internal.rule.IRuleFactory;
import org.eclipse.textmate4e.core.internal.rule.IRuleFactoryHelper;
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
		OnigString onigLineText = GrammarHelper.createOnigString(lineText);
		int lineLength = lineText.length();
		LineTokens lineTokens = new LineTokens();
		StackElement nextState = Tokenizer._tokenizeString(this, onigLineText, isFirstLine, 0, prevState, lineTokens);

		IToken[] _produced = lineTokens.getResult(nextState, lineLength);

		return new TokenizeLineResult(_produced, nextState);
	}

}
