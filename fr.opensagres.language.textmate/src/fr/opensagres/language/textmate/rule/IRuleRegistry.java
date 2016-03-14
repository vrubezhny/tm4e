package fr.opensagres.language.textmate.rule;

public interface IRuleRegistry {
 
	Rule getRule(int patternId);
	
	Rule registerRule(IRuleFactory factory);

}
