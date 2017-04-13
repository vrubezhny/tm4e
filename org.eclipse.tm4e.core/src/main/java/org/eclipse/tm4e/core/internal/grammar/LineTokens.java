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
import java.util.List;

import org.eclipse.tm4e.core.grammar.IToken;
import org.eclipse.tm4e.core.grammar.StackElement;
import org.eclipse.tm4e.core.logger.ILogger;

class LineTokens {

	private final List<IToken> _tokens;
	private final ILogger logger;
	private int _lastTokenEndIndex;
	
	LineTokens(ILogger logger) {
		this._tokens = new ArrayList<IToken>();
		this.logger = logger;
		this._lastTokenEndIndex = 0;
	}

	public void produce(StackElement stack, int endIndex) {
		produce(stack, endIndex, null);
	}

	public void produce(StackElement stack, int endIndex, List<LocalStackElement> extraScopes) {
		// console.log('PRODUCE TOKEN: lastTokenEndIndex: ' + lastTokenEndIndex
		// + ', endIndex: ' + endIndex);
		if (this._lastTokenEndIndex >= endIndex) {
			return;
		}

		List<String> scopes = stack.generateScopes();
		if (extraScopes != null) {
			for (LocalStackElement extraScope : extraScopes) {
				scopes.add(extraScope.getScopeName());
			}
		}

		this._tokens.add(new Token(this._lastTokenEndIndex, endIndex, scopes));
		this._lastTokenEndIndex = endIndex;
	}

	public IToken[] getResult(StackElement stack, int lineLength) {
		if (this._tokens.size() > 0 && this._tokens.get(this._tokens.size() - 1).getStartIndex() == lineLength - 1) {
			// pop produced token for newline
			this._tokens.remove(this._tokens.size() - 1);
		}

		if (this._tokens.size() == 0) {
			this._lastTokenEndIndex = -1;
			this.produce(stack, lineLength, null);
			this._tokens.get(this._tokens.size() - 1).setStartIndex(0);
		}

		return this._tokens.toArray(new IToken[0]);
	}
	
	public ILogger getLogger() {
		return logger;
	}
}
