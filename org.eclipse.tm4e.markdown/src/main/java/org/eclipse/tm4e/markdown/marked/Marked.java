/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * Initial code from https://github.com/chjj/marked/
 * Initial copyright Copyright (c) 2011-2014, Christopher Jeffrey and others
 * Initial license: MIT
 *
 * Contributors:
 *  - Christopher Jeffrey and others: Initial code, written in JavaScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.markdown.marked;

public class Marked {

	public static IRenderer parse(String src) {
		return parse(src, (Options) null);
	}

	public static IRenderer parse(String src, IRenderer renderer) {
		return parse(src, null, renderer);
	}

	public static IRenderer parse(String src, Options opt) {
		return parse(src, opt, null);
	}

	public static IRenderer parse(String src, Options opt, IRenderer renderer) {

		return Parser.parse(Lexer.lex(src, opt), opt, renderer);
	}
}
