package fr.opensagres.language.textmate.grammar;

import java.util.List;

import fr.opensagres.language.textmate.types.IRawGrammar;

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
