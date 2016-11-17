package org.eclipse.language.textmate.core.internal.rule;

import org.eclipse.language.textmate.core.internal.oniguruma.OnigScanner;

public class ICompiledRule {

	public OnigScanner scanner;

	public Integer[] rules;

	public ICompiledRule(OnigScanner scanner, Integer[] rules) {
		this.scanner = scanner;
		this.rules = rules;
	}
}
