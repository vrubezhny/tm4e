package _editor;

import java.util.List;

import fr.opensagres.language.textmate.grammar.StackElement;

public class TMState {

	private List<StackElement> ruleStack;

	public TMState() {
		this.ruleStack = null;
	}

	public void setRuleStack(List<StackElement> ruleStack) {
		this.ruleStack = ruleStack;
	}

	public List<StackElement> getRuleStack() {
		return ruleStack;
	}
}
