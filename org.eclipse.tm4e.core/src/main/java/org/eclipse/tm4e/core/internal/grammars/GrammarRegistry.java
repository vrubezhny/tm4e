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
