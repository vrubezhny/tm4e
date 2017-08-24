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
package org.eclipse.tm4e.languageconfiguration.internal.supports;

import java.util.AbstractMap.SimpleEntry;

/**
 * A tuple of two characters, like a pair of opening and closing brackets.
 */
@SuppressWarnings("serial")
public class CharacterPair extends SimpleEntry<String, String> {

	public CharacterPair(String key, String value) {
		super(key, value);
	}

}
