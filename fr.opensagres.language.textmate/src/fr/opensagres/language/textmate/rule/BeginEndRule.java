package fr.opensagres.language.textmate.rule;

public class BeginEndRule extends Rule {

	private _begin: RegExpSource;
public beginCaptures: CaptureRule[];
private _end: RegExpSource;
public endHasBackReferences:boolean;
public endCaptures: CaptureRule[];
public applyEndPatternLast: boolean;
public hasMissingPatterns: boolean;
public patterns: number[];
private _cachedCompiledPatterns: RegExpSourceList;

constructor(id: number, name: string, contentName: string, begin: string, beginCaptures: CaptureRule[], end: string, endCaptures: CaptureRule[], applyEndPatternLast: boolean, patterns: ICompilePatternsResult) {
	super(id, name, contentName);
	this._begin = new RegExpSource(begin, this.id);
	this.beginCaptures = beginCaptures;
	this._end = new RegExpSource(end, -1);
	this.endHasBackReferences = this._end.hasBackReferences;
	this.endCaptures = endCaptures;
	this.applyEndPatternLast = applyEndPatternLast || false;
	this.patterns = patterns.patterns;
	this.hasMissingPatterns = patterns.hasMissingPatterns;
	this._cachedCompiledPatterns = null;
}

public getEndWithResolvedBackReferences(lineText:string, captureIndices:IOnigCaptureIndex[]): string {
	return this._end.resolveBackReferences(lineText, captureIndices);
}

public collectPatternsRecursive(grammar:IRuleRegistry, out:RegExpSourceList, isFirst:boolean) {
	if (isFirst) {
		let i:number,
			len:number,
			rule:Rule;

		for (i = 0, len = this.patterns.length; i < len; i++) {
			rule = grammar.getRule(this.patterns[i]);
			rule.collectPatternsRecursive(grammar, out, false);
		}
	} else {
		out.push(this._begin);
	}
}

public compile(grammar:IRuleRegistry, endRegexSource: string, allowA:boolean, allowG:boolean): ICompiledRule {
	let precompiled = this._precompile(grammar);

	if (this._end.hasBackReferences) {
		if (this.applyEndPatternLast) {
			precompiled.setSource(precompiled.length() - 1, endRegexSource);
		} else {
			precompiled.setSource(0, endRegexSource);
		}
	}
	return this._cachedCompiledPatterns.compile(grammar, allowA, allowG);
}

private _precompile(grammar:IRuleRegistry): RegExpSourceList {
	if (!this._cachedCompiledPatterns) {
		this._cachedCompiledPatterns = new RegExpSourceList();

		this.collectPatternsRecursive(grammar, this._cachedCompiledPatterns, true);

		if (this.applyEndPatternLast) {
			this._cachedCompiledPatterns.push(this._end.hasBackReferences ? this._end.clone() : this._end);
		} else {
			this._cachedCompiledPatterns.unshift(this._end.hasBackReferences ? this._end.clone() : this._end);
		}
	}
	return this._cachedCompiledPatterns;
}

}
