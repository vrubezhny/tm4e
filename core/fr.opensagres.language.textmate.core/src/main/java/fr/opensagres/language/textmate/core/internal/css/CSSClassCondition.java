package fr.opensagres.language.textmate.core.internal.css;

import java.util.List;

public class CSSClassCondition extends CSSAttributeCondition {

	public CSSClassCondition(String localName, String namespaceURI, String value) {
		super(localName, namespaceURI, true, value);
	}
	
	@Override
	public boolean match(List<String> names) {
		return names.contains(getValue());
	}

}
