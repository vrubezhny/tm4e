package fr.opensagres.language.textmate.core.internal.rule;

import fr.opensagres.language.textmate.core.internal.oniguruma.OnigScanner;

public class ICompiledRule {

	public OnigScanner scanner;

	public Integer[] rules;

	public ICompiledRule(OnigScanner scanner, Integer[] rules) {
		this.scanner = scanner;
		this.rules = rules;
	}
}
