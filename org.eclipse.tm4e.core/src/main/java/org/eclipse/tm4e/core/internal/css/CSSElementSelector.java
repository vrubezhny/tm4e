package org.eclipse.tm4e.core.internal.css;

import java.util.List;

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
	public int nbMatch(List<String> names) {		
		return 0;
	}
	
	@Override
	public int nbClass() {
		return 0;
	}
}
