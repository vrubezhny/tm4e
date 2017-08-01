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

public class CSSElementSelector extends AbstractElementSelector {

	public CSSElementSelector(String uri, String name) {
		super(uri, name);
	}

	@Override
	public short getSelectorType() {
		return SAC_ELEMENT_NODE_SELECTOR;
	}

	@Override
	public int getSpecificity() {
		return (getLocalName() == null) ? 0 : 1;
	}

	@Override
	public int nbMatch(String... names) {		
		return 0;
	}
	
	@Override
	public int nbClass() {
		return 0;
	}
}
