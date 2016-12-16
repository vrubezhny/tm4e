package org.eclipse.tm4e.core.internal.css;

import org.w3c.css.sac.AttributeCondition;

public abstract class AbstractAttributeCondition implements AttributeCondition, ExtendedCondition {

	/**
	 * The attribute value.
	 */
	protected String value;

	/**
	 * Creates a new AbstractAttributeCondition object.
	 */
	protected AbstractAttributeCondition(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public int getSpecificity() {
		return 1 << 8;
	}
}
