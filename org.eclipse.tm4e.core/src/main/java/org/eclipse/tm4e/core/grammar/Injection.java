package org.eclipse.tm4e.core.grammar;

import org.eclipse.tm4e.core.internal.matcher.IMatcher;
import org.eclipse.tm4e.core.internal.types.IRawGrammar;

public class Injection {

	private final IMatcher<StackElement> matcher;
	public final boolean priorityMatch;
	public final int ruleId;
	public final IRawGrammar grammar;

	public Injection(IMatcher<StackElement> matcher, int ruleId, IRawGrammar grammar, boolean priorityMatch) {
		this.matcher = matcher;
		this.ruleId = ruleId;
		this.grammar = grammar;
		this.priorityMatch = priorityMatch;
	}

	public boolean match(StackElement states) {
		return matcher.match(states);
	}
}
