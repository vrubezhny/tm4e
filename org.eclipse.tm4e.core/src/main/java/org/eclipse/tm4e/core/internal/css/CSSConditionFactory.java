package org.eclipse.tm4e.core.internal.css;

import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionFactory;
import org.w3c.css.sac.ContentCondition;
import org.w3c.css.sac.LangCondition;
import org.w3c.css.sac.NegativeCondition;
import org.w3c.css.sac.PositionalCondition;

public class CSSConditionFactory implements ConditionFactory {

	public static final ConditionFactory INSTANCE = new CSSConditionFactory();

	@Override
	public AttributeCondition createClassCondition(String namespaceURI, String value) throws CSSException {
		return new CSSClassCondition(null, "class", value);
	}

	@Override
	public AttributeCondition createAttributeCondition(String localName, String namespaceURI, boolean specified,
			String value) throws CSSException {
		return new CSSAttributeCondition(localName, namespaceURI, specified, value);
	}

	@Override
	public CombinatorCondition createAndCondition(Condition first,
			Condition second) throws CSSException {
		return new CSSAndCondition(first, second);
	}

	@Override
	public AttributeCondition createBeginHyphenAttributeCondition(String arg0, String arg1, boolean arg2, String arg3)
			throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public ContentCondition createContentCondition(String arg0) throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public AttributeCondition createIdCondition(String arg0) throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public LangCondition createLangCondition(String arg0) throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public NegativeCondition createNegativeCondition(Condition arg0) throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public AttributeCondition createOneOfAttributeCondition(String arg0, String arg1, boolean arg2, String arg3)
			throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public Condition createOnlyChildCondition() throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public Condition createOnlyTypeCondition() throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public CombinatorCondition createOrCondition(Condition arg0, Condition arg1) throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public PositionalCondition createPositionalCondition(int arg0, boolean arg1, boolean arg2) throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

	@Override
	public AttributeCondition createPseudoClassCondition(String arg0, String arg1) throws CSSException {
		throw new CSSException("Not implemented in CSS2");
	}

}
