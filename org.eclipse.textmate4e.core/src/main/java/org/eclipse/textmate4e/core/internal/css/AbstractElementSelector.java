package org.eclipse.textmate4e.core.internal.css;

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
