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
package org.eclipse.tm4e.languageconfiguration.internal.utils;

import java.util.regex.Pattern;

/**
 * Regex utilities.
 *
 */
public class RegExpUtils {

	/**
	 * Escapes regular expression characters in a given string
	 */
	public static String escapeRegExpCharacters(String value) {
		return value.replaceAll("[\\-\\\\\\{\\}\\*\\+\\?\\|\\^\\$\\.\\[\\]\\(\\)\\#]", "\\\\$0"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Create Java Regexp and null otherwise.
	 *
	 * @param regex
	 * @return Java Regexp and null otherwise.
	 */
	public static Pattern create(String regex) {
		try {
			return Pattern.compile(regex);
		} catch (Exception e) {
			return null;
		}
	}
}
