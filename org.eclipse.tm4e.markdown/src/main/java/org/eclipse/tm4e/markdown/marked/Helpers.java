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

public class Helpers {

	public static String escape(String html) {
		return escape(html, false);
	}

	public static String escape(String html, boolean encode) {
		return html
			    .replaceAll(!encode ? "&(?!#?\\w+;)" : "&", "&amp;")
			    .replaceAll("<", "&lt;")
			    .replaceAll(">", "&gt;")
			    .replaceAll("\"", "&quot;")
			    .replaceAll("'", "&#39;");
	}

	public static boolean isEmpty(String s) {
		return s == null || s.length() < 1;
	}

}
