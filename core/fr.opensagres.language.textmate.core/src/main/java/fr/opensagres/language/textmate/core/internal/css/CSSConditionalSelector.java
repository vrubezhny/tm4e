package fr.opensagres.language.textmate.core.internal.css;

import java.util.List;

import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.SimpleSelector;

public class CSSConditionalSelector implements ConditionalSelector, ExtendedSelector {

	/**
	 * The simple selector.
	 */
	protected SimpleSelector simpleSelector;

	/**
	 * The condition.
	 */
	protected Condition condition;

	/**
	 * Creates a new ConditionalSelector object.
	 */
	public CSSConditionalSelector(SimpleSelector simpleSelector, Condition condition) {
		this.simpleSelector = simpleSelector;
		this.condition = condition;
	}

	@Override
	public short getSelectorType() {
		return SAC_CONDITIONAL_SELECTOR;
	}

	@Override
	public Condition getCondition() {
		return condition;
	}

	@Override
	public SimpleSelector getSimpleSelector() {
		return simpleSelector;
	}

	@Override
	public int getSpecificity() {
		return ((ExtendedSelector) getSimpleSelector()).getSpecificity()
				+ ((ExtendedCondition) getCondition()).getSpecificity();
	}

	@Override
	public boolean match(List<String> names) {
		return ((ExtendedSelector)getSimpleSelector()).match(names) &&
	               ((ExtendedCondition)getCondition()).match(names);
	}

}
