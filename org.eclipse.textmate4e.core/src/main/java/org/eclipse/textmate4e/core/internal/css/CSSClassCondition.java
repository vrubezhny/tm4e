package org.eclipse.textmate4e.core.internal.css;

import java.util.List;

public class CSSClassCondition extends CSSAttributeCondition {

	public CSSClassCondition(String localName, String namespaceURI, String value) {
		super(localName, namespaceURI, true, value);
	}
	
	@Override
	public int nbMatch(List<String> names) {
		return names.contains(getValue()) ? 1 : 0;
	}
	
	@Override
	public int nbClass() {
		return 1;
	}

}
