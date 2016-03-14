package fr.opensagres.language.textmate.rule;

public class ICompiledRule {

	public /* OnigScanner */ Object scanner;

	public Integer[] rules;
	
	public ICompiledRule(Object scanner, Integer[] rules) {
		this.scanner = scanner;
		this.rules = rules;
	}
}
