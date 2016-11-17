package org.eclipse.language.textmate.core.internal.rule;

public interface IRuleRegistry {
 
	Rule getRule(int patternId);
	
	Rule registerRule(IRuleFactory factory);

}
