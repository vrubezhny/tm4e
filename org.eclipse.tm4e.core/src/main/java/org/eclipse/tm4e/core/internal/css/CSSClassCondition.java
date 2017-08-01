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
package org.eclipse.tm4e.core.internal.css;

public class CSSClassCondition extends CSSAttributeCondition {

	public CSSClassCondition(String localName, String namespaceURI, String value) {
		super(localName, namespaceURI, true, value);
	}

	@Override
	public int nbMatch(String... names) {
		String value = getValue();
		for (String name : names) {
			if (name.equals(value)) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int nbClass() {
		return 1;
	}

}
