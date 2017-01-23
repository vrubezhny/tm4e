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

import org.w3c.css.sac.ElementSelector;

public abstract class AbstractElementSelector implements ElementSelector, ExtendedSelector {

	/**
	 * The namespace URI.
	 */
	protected String namespaceURI;

	/**
	 * The local name.
	 */
	protected String localName;

	/**
	 * Creates a new ElementSelector object.
	 */
	protected AbstractElementSelector(String uri, String name) {
		namespaceURI = uri;
		localName = name;
	}

	@Override
	public String getNamespaceURI() {
		return namespaceURI;
	}

	@Override
	public String getLocalName() {
		return localName;
	}

}
