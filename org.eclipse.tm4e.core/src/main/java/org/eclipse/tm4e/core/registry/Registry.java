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
package org.eclipse.tm4e.core.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.tm4e.core.TMException;
import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.internal.grammar.reader.GrammarReader;
import org.eclipse.tm4e.core.internal.types.IRawGrammar;

public class Registry {

	private final IGrammarLocator _locator;
	private final SyncRegistry _syncRegistry;

	public Registry() {
		this(IGrammarLocator.DEFAULT_LOCATOR);
	}

	public Registry(IGrammarLocator locator) {
		this._locator = locator;
		this._syncRegistry = new SyncRegistry();
	}

	public IGrammar loadGrammar(String initialScopeName) {

		List<String> remainingScopeNames = new ArrayList<>();
		remainingScopeNames.add(initialScopeName);

		List<String> seenScopeNames = new ArrayList<>();
		seenScopeNames.add(initialScopeName);

		while (!remainingScopeNames.isEmpty()) {
			String scopeName = remainingScopeNames.remove(0); // shift();

			if (this._syncRegistry.lookup(scopeName) != null) {
				continue;
			}

			String filePath = this._locator.getFilePath(scopeName);
			if (filePath == null) {
				if (scopeName.equals(initialScopeName)) {
					// System.err.println();
					throw new TMException("Unknown location for grammar <" + initialScopeName + ">");
					// callback(new Error('Unknown location for grammar <' +
					// initialScopeName + '>'), null);
					// return;
				}
				continue;
			}

			try {
				InputStream in = this._locator.getInputStream(scopeName);
				IRawGrammar grammar = GrammarReader.readGrammarSync(filePath, in);
				Collection<String> injections = this._locator.getInjections(scopeName);

				Collection<String> deps = this._syncRegistry.addGrammar(grammar, injections);
				for (String dep : deps) {
					if (!seenScopeNames.contains(dep)) {
						seenScopeNames.add(dep);
						remainingScopeNames.add(dep);
					}
				}
			} catch (Throwable e) {
				if (scopeName.equals(initialScopeName)) {
					// callback(new Error('Unknown location for grammar <' +
					// initialScopeName + '>'), null);
					// return;
					throw new TMException("Unknown location for grammar <" + initialScopeName + ">");
				}
			}
		}
		return this.grammarForScopeName(initialScopeName);
	}

	public IGrammar loadGrammarFromPathSync(File file) throws Exception {
		return loadGrammarFromPathSync(file.getPath(), new FileInputStream(file));
	}

	public IGrammar loadGrammarFromPathSync(String path, InputStream in) throws Exception {
		IRawGrammar rawGrammar = GrammarReader.readGrammarSync(path, in);
		Collection<String> injections = this._locator.getInjections(rawGrammar.getScopeName());
		this._syncRegistry.addGrammar(rawGrammar, injections);
		return this.grammarForScopeName(rawGrammar.getScopeName());
	}

	public IGrammar grammarForScopeName(String scopeName) {
		return this._syncRegistry.grammarForScopeName(scopeName);
	}

}
