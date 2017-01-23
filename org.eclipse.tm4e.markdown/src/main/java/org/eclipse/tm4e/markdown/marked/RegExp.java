/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * This code is an translation of code copyrighted by https://github.com/chjj/marked, and initially licensed under MIT.
 *
 * Contributors:
 *  - https://github.com/chjj/marked: Initial code, written in JavaScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.markdown.marked;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {

	protected String source;
	private Pattern pattern;

	public RegExp(String source) {
		this.source = source;
	}

	public Matcher exec(String s) {
		if (source == null) {
			return null;
		}
		if (pattern == null) {
			pattern = Pattern.compile(source);
		}
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			return matcher;
		}
		return null;
	}

	public RegExp replace(String name, RegExp val) {
		return replace(name, val.source);
	}

	public RegExp replace(String name, String val) {
		if (name == null)
			return new RegExp(this.source);
		val = val.replaceAll("(^|[^\\[])\\^", "$1");
		this.source = this.source.replaceFirst(name, Matcher.quoteReplacement(val));
		return this;
	}

	public RegExp replaceAll(String name, RegExp val) {
		return replaceAll(name, val.source);
	}

	public RegExp replaceAll(String name, String val) {
		if (name == null)
			return new RegExp(this.source);
		val = val.replaceAll("(^|[^\\[])\\^", "$1");
		this.source = this.source.replaceAll(name, Matcher.quoteReplacement(val));
		return this;
	}

	public static final RegExp noop() {
		return new RegExp(null);
	};

}
