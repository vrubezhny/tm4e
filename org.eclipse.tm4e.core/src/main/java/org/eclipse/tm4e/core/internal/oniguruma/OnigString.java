/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.core.internal.oniguruma;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Oniguruma string.
 * 
 * @see https://github.com/atom/node-oniguruma/blob/master/src/onig-string.cc
 *
 */
public class OnigString {

	private static final String UTF_8 = "UTF-8";
	
	private final String str;
	private byte[] value;
	private UUID uniqueId;

	public OnigString(String str) {
		this.str = str;
		this.value = str.getBytes(Charset.forName(UTF_8));
		this.uniqueId = UUID.randomUUID();
	}

	public UUID uniqueId() {
		return uniqueId;
	}

	public byte[] utf8_value() {
		return value;
	}

	public int utf8_length() {
		return str.length();
	}

	public String getString() {
		return str;
	}
}
