package org.eclipse.textmate4e.core.internal.rule;

public class IncludeOnlyRule extends Rule {

	public boolean hasMissingPatterns;
	public Integer[] patterns;
	private RegExpSourceList _cachedCompiledPatterns;

	public IncludeOnlyRule(int id, String name, String contentName, ICompilePatternsResult patterns) {
		super(id, name, contentName);
		this.patterns = patterns.patterns;
		this.hasMissingPatterns = patterns.hasMissingPatterns;
		this._cachedCompiledPatterns = null;
	}

	public void collectPatternsRecursive(IRuleRegistry grammar, RegExpSourceList out, boolean isFirst) {
		int i;
		int len;
		Rule rule;

		for (i = 0, len = this.patterns.length; i < len; i++) {
			rule = grammar.getRule(this.patterns[i]);
			rule.collectPatternsRecursive(grammar, out, false);
		}
	}

	public ICompiledRule compile(IRuleRegistry grammar, String endRegexSource, boolean allowA, boolean allowG) {
		if (this._cachedCompiledPatterns == null) {
			this._cachedCompiledPatterns = new RegExpSourceList();
			this.collectPatternsRecursive(grammar, this._cachedCompiledPatterns, true);
		}
		return this._cachedCompiledPatterns.compile(grammar, allowA, allowG);
	}

}
