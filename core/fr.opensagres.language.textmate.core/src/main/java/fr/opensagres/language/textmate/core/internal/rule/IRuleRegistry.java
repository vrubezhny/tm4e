package fr.opensagres.language.textmate.core.internal.rule;

public interface IRuleRegistry {
 
	Rule getRule(int patternId);
	
	Rule registerRule(IRuleFactory factory);

}
