package fr.opensagres.language.textmate.rule;

import java.util.Collection;

public class MatchRule extends Rule {

	private RegExpSource _match;
	public Collection<CaptureRule> captures;
	private RegExpSourceList _cachedCompiledPatterns;

	public MatchRule(int id, String name, String match, Collection<CaptureRule> captures) {
		super(id, name, null);
		this._match = new RegExpSource(match, this.id);
		this.captures = captures;
		this._cachedCompiledPatterns = null;
	}

	public void collectPatternsRecursive(IRuleRegistry grammar, RegExpSourceList out, boolean isFirst) {
		out.push(this._match);
	}

	public ICompiledRule compile(IRuleRegistry grammar, String endRegexSource, boolean allowA, boolean allowG) {
		if (this._cachedCompiledPatterns == null) {
			this._cachedCompiledPatterns = new RegExpSourceList();
			this.collectPatternsRecursive(grammar, this._cachedCompiledPatterns, true);
		}
		return this._cachedCompiledPatterns.compile(grammar, allowA, allowG);
	}
}
