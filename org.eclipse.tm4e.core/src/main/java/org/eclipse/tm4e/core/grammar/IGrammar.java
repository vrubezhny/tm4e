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
package org.eclipse.tm4e.core.grammar;

/**
 * TextMate grammar API.
 * 
 * @see https://github.com/Microsoft/vscode-textmate/blob/master/src/main.ts
 *
 */
public interface IGrammar {

	/**
	 * Returns the scope name of the grammar.
	 * 
	 * @return the scope name of the grammar.
	 */
	String getScopeName();

	/**
	 * Tokenize `lineText`.
	 * 
	 * @param lineText
	 *            the line text to tokenize.
	 * @return the result of the tokenization.
	 */
	ITokenizeLineResult tokenizeLine(String lineText);

	/**
	 * Tokenize `lineText` using previous line state `prevState`.
	 * 
	 * @param lineText
	 *            the line text to tokenize.
	 * @param prevState
	 *            previous line state.
	 * @return the result of the tokenization.
	 */
	ITokenizeLineResult tokenizeLine(String lineText, StackElement prevState);

}
