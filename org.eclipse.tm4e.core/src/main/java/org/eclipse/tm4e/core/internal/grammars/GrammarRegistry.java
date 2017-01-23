/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.core.internal.grammars;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.registry.IGrammarLocator;
import org.eclipse.tm4e.core.registry.Registry;

public class GrammarRegistry extends Registry {

	private Map<String, GrammarDefinition> definitions;

	public GrammarRegistry(IGrammarLocator locator) {
		super(locator);
		this.definitions = new HashMap<>();
	}

	public void register(GrammarDefinition definition) {
		definitions.put(definition.getScopeName(), definition);
	}

	public IGrammar getGrammar(String scopeName) {
		IGrammar grammar = super.grammarForScopeName(scopeName);
		if (grammar != null) {
			return grammar;
		}
		return super.loadGrammar(scopeName);
	}

	public GrammarDefinition getDefinition(String scopeName) {
		return definitions.get(scopeName);
	}

}
