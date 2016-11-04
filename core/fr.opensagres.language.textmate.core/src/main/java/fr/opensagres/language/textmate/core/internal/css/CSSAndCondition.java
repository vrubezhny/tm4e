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
	public boolean match(List<String> names) {
		return ((ExtendedCondition) getFirstCondition()).match(names)
				&& ((ExtendedCondition) getSecondCondition()).match(names);
	}
}
