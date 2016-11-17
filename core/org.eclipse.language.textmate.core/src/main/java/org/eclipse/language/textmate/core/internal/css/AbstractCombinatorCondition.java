package org.eclipse.language.textmate.core.internal.css;

import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;

public abstract class AbstractCombinatorCondition implements CombinatorCondition, ExtendedCondition {
	protected Condition firstCondition;

	protected Condition secondCondition;

	/**
	 * Creates a new CombinatorCondition object.
	 */
	protected AbstractCombinatorCondition(Condition c1, Condition c2) {
		firstCondition = c1;
		secondCondition = c2;
	}

	@Override
	public Condition getFirstCondition() {
		return firstCondition;
	}

	@Override
	public Condition getSecondCondition() {
		return secondCondition;
	}

	public int getSpecificity() {
		return ((ExtendedCondition) getFirstCondition()).getSpecificity()
				+ ((ExtendedCondition) getSecondCondition()).getSpecificity();
	}
}
