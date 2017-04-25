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
package org.eclipse.tm4e.registry.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.logger.ILogger;
import org.eclipse.tm4e.core.registry.IGrammarLocator;
import org.eclipse.tm4e.core.registry.Registry;

/**
 * Eclipse grammar registry.
 *
 */
public class GrammarRegistry extends Registry {

	private final Map<String, GrammarDefinition> definitions;
	private final Map<String, Collection<String>> injections;

	public GrammarRegistry(IGrammarLocator locator, ILogger logger) {
		super(locator, logger);
		this.definitions = new HashMap<>();
		this.injections = new HashMap<>();
	}

	/**
	 * Register a grammar definition.
	 * 
	 * @param definition
	 *            the grammar definition to register.
	 */
	public void register(GrammarDefinition definition) {
		definitions.put(definition.getScopeName(), definition);
	}

	/**
	 * Returns the loaded grammar from the given <code>scopeName</code> and null
	 * otherwise.
	 * 
	 * @param scopeName
	 * @return the loaded grammar from the given <code>scopeName</code> and null
	 *         otherwise.
	 */
	public IGrammar getGrammar(String scopeName) {
		IGrammar grammar = super.grammarForScopeName(scopeName);
		if (grammar != null) {
			return grammar;
		}
		return super.loadGrammar(scopeName);
	}

	/**
	 * Returns the grammar definition from the given <code>scopeName</code> and
	 * null otherwise.
	 * 
	 * @param scopeName
	 * @return the grammar definition from the given <code>scopeName</code> and
	 *         null otherwise.
	 */
	public GrammarDefinition getDefinition(String scopeName) {
		return definitions.get(scopeName);
	}

	/**
	 * Returns list of scope names to inject for the given
	 * <code>scopeName</code> and null otheriwse.
	 * 
	 * @param scopeName
	 * @return list of scope names to inject for the given
	 *         <code>scopeName</code> and null otheriwse.
	 */
	public Collection<String> getInjections(String scopeName) {
		return injections.get(scopeName);
	}

	/**
	 * Register the given <code>scopeName</code> to inject to the given scope
	 * name <code>injectTo</code>.
	 * 
	 * @param scopeName
	 * @param injectTo
	 */
	public void registerInjection(String scopeName, String injectTo) {
		Collection<String> injections = getInjections(injectTo);
		if (injections == null) {
			injections = new ArrayList<>();
			this.injections.put(injectTo, injections);
		}
		injections.add(scopeName);
	}

}
