package org.eclipse.textmate4e.core.grammar;

import java.util.List;

public class RawTestLine {

	private String line;
	private List<RawToken> tokens;

	public String getLine() {
		return line;
	}
	
	public List<RawToken> getTokens() {
		return tokens;
	}
}
