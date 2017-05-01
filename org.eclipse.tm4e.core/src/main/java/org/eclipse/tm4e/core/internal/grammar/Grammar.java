/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial code from https://github.com/Microsoft/vscode-textmate/
 * Initial copyright Copyright (C) Microsoft Corporation. All rights reserved.
 * Initial license: MIT
 *
 * Contributors:
 *  - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.core.internal.grammar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.tm4e.core.grammar.GrammarHelper;
import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.grammar.IGrammarRepository;
import org.eclipse.tm4e.core.grammar.IToken;
import org.eclipse.tm4e.core.grammar.ITokenizeLineResult;
import org.eclipse.tm4e.core.grammar.Injection;
import org.eclipse.tm4e.core.grammar.StackElement;
import org.eclipse.tm4e.core.internal.grammar.parser.Raw;
import org.eclipse.tm4e.core.internal.matcher.Matcher;
import org.eclipse.tm4e.core.internal.oniguruma.OnigString;
import org.eclipse.tm4e.core.internal.rule.IRuleFactory;
import org.eclipse.tm4e.core.internal.rule.IRuleFactoryHelper;
import org.eclipse.tm4e.core.internal.rule.Rule;
import org.eclipse.tm4e.core.internal.rule.RuleFactory;
import org.eclipse.tm4e.core.internal.types.IRawGrammar;
import org.eclipse.tm4e.core.internal.types.IRawRepository;
import org.eclipse.tm4e.core.internal.types.IRawRule;

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
		this._injections = null;
	}

	public List<Injection> getInjections(StackElement states) {
		if (this._injections == null) {
			this._injections = new ArrayList<Injection>();
			// add injections from the current grammar
			Map<String, IRawRule> rawInjections = this._grammar.getInjections();
			if (rawInjections != null) {
				for (Entry<String, IRawRule> injection : rawInjections.entrySet()) {
					String expression = injection.getKey();
					IRawRule rule = injection.getValue();
					collectInjections(this._injections, expression, rule, this, this._grammar);
				}
			}

			// add injection grammars contributed for the current scope
			if (this._grammarRepository != null) {
				Collection<String> injectionScopeNames = this._grammarRepository
						.injections(this._grammar.getScopeName());
				if (injectionScopeNames != null) {
					injectionScopeNames.forEach(injectionScopeName -> {
						IRawGrammar injectionGrammar = this.getExternalGrammar(injectionScopeName);
						if (injectionGrammar != null) {
							String selector = injectionGrammar.getInjectionSelector();
							if (selector != null) {
								collectInjections(this._injections, selector, (IRawRule) injectionGrammar, this,
										injectionGrammar);
							}
						}
					});
				}
			}
		}
		if (this._injections.size() == 0) {
			return this._injections;
		}
		return this._injections.stream().filter(injection -> injection.match(states)).collect(Collectors.toList());
	}

	private void collectInjections(List<Injection> result, String selector, IRawRule rule,
			IRuleFactoryHelper ruleFactoryHelper, IRawGrammar grammar) {
		String[] subExpressions = selector.split(",");
		Arrays.stream(subExpressions).forEach(subExpression -> {
			String expressionString = subExpression.replaceAll("L:", "");
			result.add(new Injection(Matcher.createMatcher(expressionString),
					RuleFactory.getCompiledRuleId(rule, ruleFactoryHelper, grammar.getRepository()), grammar,
					expressionString.length() < subExpression.length()));
		});
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

	public IRawGrammar getExternalGrammar(String scopeName) {
		return getExternalGrammar(scopeName, null);
	}

	@Override
	public IRawGrammar getExternalGrammar(String scopeName, IRawRepository repository) {
		if (this._includedGrammars.containsKey(scopeName)) {
			return this._includedGrammars.get(scopeName);
		} else if (this._grammarRepository != null) {
			IRawGrammar rawIncludedGrammar = this._grammarRepository.lookup(scopeName);
			if (rawIncludedGrammar != null) {
				this._includedGrammars.put(scopeName,
						initGrammar(rawIncludedGrammar, repository != null ? repository.getBase() : null));
				return this._includedGrammars.get(scopeName);
			}
		}
		return null;
	}

	private IRawGrammar initGrammar(IRawGrammar grammar, IRawRule base) {
		grammar = clone(grammar);
		if (grammar.getRepository() == null) {
			((Raw) grammar).setRepository(new Raw());
		}
		Raw self = new Raw();
		self.setPatterns(grammar.getPatterns());
		self.setName(grammar.getScopeName());
		grammar.getRepository().setSelf(self);
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
		LineTokens lineTokens = new LineTokens(this._grammarRepository.getLogger());
		StackElement nextState = LineTokenizer._tokenizeString(this, onigLineText, isFirstLine, 0, prevState,
				lineTokens);

		IToken[] _produced = lineTokens.getResult(nextState, lineLength);

		return new TokenizeLineResult(_produced, nextState);
	}

	@Override
	public String getScopeName() {
		return _grammar.getScopeName();
	}
	
	@Override
	public Collection<String> getFileTypes() {
		return _grammar.getFileTypes();
	}
	
}
