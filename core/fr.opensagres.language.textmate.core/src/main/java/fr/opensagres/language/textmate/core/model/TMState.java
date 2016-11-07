package fr.opensagres.language.textmate.core.model;

import java.util.ArrayList;
import java.util.List;

import fr.opensagres.language.textmate.core.grammar.StackElement;

public class TMState {

	private List<StackElement> ruleStack;

	public TMState(List<StackElement> ruleStatck) {
		this.ruleStack = ruleStatck;
	}

	public void setRuleStack(List<StackElement> ruleStack) {
		this.ruleStack = ruleStack;
	}

	public List<StackElement> getRuleStack() {
		return ruleStack;
	}

	public TMState clone() {
		return new TMState(ruleStack != null ? new ArrayList<StackElement>(ruleStack) : null);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof TMState)) {
			return false;
		}
		TMState otherState = (TMState) other;

		// Equals on `_parentEmbedderState`
		// if (!AbstractState.safeEquals(this._parentEmbedderState,
		// otherState._parentEmbedderState)) {
		// return false;
		// }

		// Equals on `_ruleStack`
		if (this.ruleStack == null && otherState.ruleStack == null) {
			return true;
		}
		if (this.ruleStack == null || otherState.ruleStack == null) {
			return false;
		}
		return this.ruleStack.equals(otherState.ruleStack);
	}
}
