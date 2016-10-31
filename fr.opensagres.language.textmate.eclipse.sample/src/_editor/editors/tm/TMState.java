package _editor.editors.tm;

import java.util.List;

import fr.opensagres.language.textmate.grammar.StackElement;

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
		return new TMState(ruleStack);
	}
}
