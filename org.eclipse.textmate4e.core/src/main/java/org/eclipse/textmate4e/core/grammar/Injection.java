package org.eclipse.textmate4e.core.grammar;

import java.util.List;

import org.eclipse.textmate4e.core.internal.types.IRawGrammar;

public class Injection {

	private final Matcher<List<StackElement>> matcher;
	public final boolean priorityMatch;
	public final int ruleId;
	public final IRawGrammar grammar;

	public Injection(Matcher<List<StackElement>> matcher, int ruleId, IRawGrammar grammar, boolean priorityMatch) {
		this.matcher = matcher;
		this.ruleId = ruleId;
		this.grammar = grammar;
		this.priorityMatch = priorityMatch;
	}

	public boolean match(List<StackElement> states) {
		// TODO Auto-generated method stub
		return false;
	}
}
