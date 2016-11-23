package org.eclipse.textmate4e.core.internal.css;

import java.util.List;

public class CSSAttributeCondition extends AbstractAttributeCondition {

	/**
	 * The attribute's local name.
	 */
	protected String localName;

	/**
	 * The attribute's namespace URI.
	 */
	protected String namespaceURI;

	/**
	 * Whether this condition applies to specified attributes.
	 */
	protected boolean specified;

	public CSSAttributeCondition(String localName, String namespaceURI, boolean specified, String value) {
		super(value);
		this.localName = localName;
		this.namespaceURI = namespaceURI;
		this.specified = specified;
	}

	@Override
	public String getLocalName() {
		return localName;
	}

	@Override
	public String getNamespaceURI() {
		return namespaceURI;
	}

	@Override
	public boolean getSpecified() {
		return specified;
	}

	@Override
	public short getConditionType() {
		return SAC_ATTRIBUTE_CONDITION;
	}

	@Override
	public int nbMatch(List<String> names) {
//		String val = getValue();
//		if (val == null) {
//			return !e.getAttribute(getLocalName()).equals("");
//		}
//		return e.getAttribute(getLocalName()).equals(val);
		return 0;
	}
	
	@Override
	public int nbClass() {
		return 0;
	}
}
