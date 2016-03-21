package fr.opensagres.language.textmate.rule;

import fr.opensagres.language.textmate.oniguruma.IOnigCaptureIndex;

public class RegExpSource {

	// private static final String HAS_BACK_REFERENCES = "/\\(\d+)/";

	public int ruleId;
	public boolean hasAnchor;
	public boolean hasBackReferences;
	private IRegExpSourceAnchorCache _anchorCache;
	public String source;

	public RegExpSource(String regExpSource, int ruleId) {
		this(regExpSource, ruleId, true);
	}

	public RegExpSource(String regExpSource, int ruleId, boolean handleAnchors) {
		if (handleAnchors) {
			this._handleAnchors(regExpSource);
		} else {
			this.source = regExpSource;
			this.hasAnchor = false;
		}

		if (this.hasAnchor) {
			this._anchorCache = this._buildAnchorCache();
		}

		this.ruleId = ruleId;

		// TODO !!!!!!!!!!!!!!
		this.hasBackReferences = false; // HAS_BACK_REFERENCES.test(this.source);

		// console.log('input: ' + regExpSource + ' => ' + this.source + ', ' +
		// this.hasAnchor);
	}

	public RegExpSource clone() {
		return new RegExpSource(this.source, this.ruleId, true);
	}

	public void setSource(String newSource) {
		if (this.source.equals(newSource)) {
			return;
		}
		this.source = newSource;

		if (this.hasAnchor) {
			this._anchorCache = this._buildAnchorCache();
		}
	}

	private void _handleAnchors(String regExpSource) {
		if (regExpSource != null) {
			int pos;
			int len;
			char ch;
			char nextCh;
			int lastPushedPos = 0;
			StringBuilder output = new StringBuilder();

			boolean hasAnchor = false;
			for (pos = 0, len = regExpSource.length(); pos < len; pos++) {
				ch = regExpSource.charAt(pos);

				if (ch == '\\') {
					if (pos + 1 < len) {
						nextCh = regExpSource.charAt(pos + 1);
						if (nextCh == 'z') {
							output.append(regExpSource.substring(lastPushedPos, pos));
							output.append("$(?!\\n)(?<!\\n)");
							lastPushedPos = pos + 2;
						} else if (nextCh == 'A' || nextCh == 'G') {
							hasAnchor = true;
						}
						pos++;
					}
				}
			}

			this.hasAnchor = hasAnchor;
			if (lastPushedPos == 0) {
				// No \z hit
				this.source = regExpSource;
			} else {
				output.append(regExpSource.substring(lastPushedPos, len));
				this.source = output.toString(); // join('');
			}
		} else {
			this.hasAnchor = false;
			this.source = regExpSource;
		}
	}

	public String resolveBackReferences(String lineText, IOnigCaptureIndex[] captureIndices) {
		/*
		 * let capturedValues = captureIndices.map((capture) => { return
		 * lineText.substring(capture.start, capture.end); });
		 * BACK_REFERENCING_END.lastIndex = 0; return
		 * this.source.replace(BACK_REFERENCING_END, (match, g1) => { return
		 * escapeRegExpCharacters(capturedValues[parseInt(g1, 10)] || ''); });
		 */
		// TODO!!!!!!!!!!!!!!!!!
		return "";
	}

	private IRegExpSourceAnchorCache _buildAnchorCache() {

		// Collection<String> A0_G0_result=new ArrayList<Character>();
		// Collection<String> A0_G1_result=new ArrayList<String>();
		// Collection<String> A1_G0_result=new ArrayList<String>();
		// Collection<String> A1_G1_result=new ArrayList<String>();

		StringBuilder A0_G0_result = new StringBuilder();
		StringBuilder A0_G1_result = new StringBuilder();
		StringBuilder A1_G0_result = new StringBuilder();
		StringBuilder A1_G1_result = new StringBuilder();
		int pos;
		int len;
		char ch;
		char nextCh;

		for (pos = 0, len = this.source.length(); pos < len; pos++) {
			ch = this.source.charAt(pos);
			A0_G0_result.append(ch);
			A0_G1_result.append(ch);
			A1_G0_result.append(ch);
			A1_G1_result.append(ch);

			if (ch == '\\') {
				if (pos + 1 < len) {
					nextCh = this.source.charAt(pos + 1);
					if (nextCh == 'A') {
						A0_G0_result.append('\uFFFF');
						A0_G1_result.append('\uFFFF');
						A1_G0_result.append('A');
						A1_G1_result.append('A');
					} else if (nextCh == 'G') {
						A0_G0_result.append('\uFFFF');
						A0_G1_result.append('G');
						A1_G0_result.append('\uFFFF');
						A1_G1_result.append('G');
					} else {
						A0_G0_result.append(nextCh);
						A0_G1_result.append(nextCh);
						A1_G0_result.append(nextCh);
						A1_G1_result.append(nextCh);
					}
					pos++;
				}
			}
		}

		return new IRegExpSourceAnchorCache(A0_G0_result.toString(), A0_G1_result.toString(), A1_G0_result.toString(),
				A1_G1_result.toString()
		// StringUtils.join(A0_G0_result, ""),
		// StringUtils.join(A0_G1_result, ""),
		// StringUtils.join(A1_G0_result, ""),
		// StringUtils.join(A1_G1_result, "")
		);
	}

	public String resolveAnchors(boolean allowA, boolean allowG) {
		if (!this.hasAnchor) {
			return this.source;
		}

		if (allowA) {
			if (allowG) {
				return this._anchorCache.A1_G1;
			} else {
				return this._anchorCache.A1_G0;
			}
		} else {
			if (allowG) {
				return this._anchorCache.A0_G1;
			} else {
				return this._anchorCache.A0_G0;
			}
		}
	}

}
