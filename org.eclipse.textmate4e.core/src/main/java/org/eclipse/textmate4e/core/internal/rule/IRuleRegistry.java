package org.eclipse.textmate4e.core.internal.rule;

public interface IRuleRegistry {
 
	Rule getRule(int patternId);
	
	Rule registerRule(IRuleFactory factory);

}
