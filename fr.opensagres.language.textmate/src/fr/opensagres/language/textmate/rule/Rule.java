package fr.opensagres.language.textmate.rule;

import fr.opensagres.language.textmate.utils.RegexSource;

public class Rule {

	public int id;

	private boolean _nameIsCapturing;
	private String _name;

	private boolean _contentNameIsCapturing;
	private String _contentName;

	public Rule(int id, String name, String contentName) {
		this.id = id;
		this._name = name; // || null;
		this._nameIsCapturing = RegexSource.hasCaptures(this._name);
		this._contentName = contentName; // || null;
		this._contentNameIsCapturing = RegexSource.hasCaptures(this._contentName);
	}

	public String getName(String lineText, /*IOnigCaptureIndex[]*/ Object captureIndices) {
		if (!this._nameIsCapturing) {
			return this._name;
		}
		return RegexSource.replaceCaptures(this._name, lineText, captureIndices);
	}

	public String getContentName(String lineText, /*IOnigCaptureIndex[]*/ Object captureIndices) {
		if (!this._contentNameIsCapturing) {
			return this._contentName;
		}
		return RegexSource.replaceCaptures(this._contentName, lineText, captureIndices);
	}

	public void collectPatternsRecursive(IRuleRegistry grammar, RegExpSourceList out, boolean isFirst) {
		throw new UnsupportedOperationException("Implement me!");
	}

	public ICompiledRule compile(IRuleRegistry grammar, String endRegexSource, boolean allowA, boolean allowG) {
		throw new UnsupportedOperationException("Implement me!");
	}

}