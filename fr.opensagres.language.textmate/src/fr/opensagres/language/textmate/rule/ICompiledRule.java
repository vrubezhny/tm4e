package fr.opensagres.language.textmate.rule;

public interface ICompiledRule {

	/*OnigScanner*/ Object getScanner();
	
	int[] getRules();
}
