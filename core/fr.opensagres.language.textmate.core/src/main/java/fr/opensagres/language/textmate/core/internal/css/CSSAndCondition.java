package fr.opensagres.language.textmate.core.internal.css;

import java.util.List;

import org.w3c.css.sac.Condition;

public class CSSAndCondition extends AbstractCombinatorCondition {

	/**
	 * Creates a new CombinatorCondition object.
	 */
	public CSSAndCondition(Condition c1, Condition c2) {
		super(c1, c2);
	}

	@Override
	public short getConditionType() {
		return SAC_AND_CONDITION;
	}

	@Override
	public int nbMatch(List<String> names) {
		return ((ExtendedCondition) getFirstCondition()).nbMatch(names)
				+ ((ExtendedCondition) getSecondCondition()).nbMatch(names);
	}
	
	 @Override
	public int nbClass() {
		 return ((ExtendedCondition) getFirstCondition()).nbClass()
					+ ((ExtendedCondition) getSecondCondition()).nbClass();
	}
}
