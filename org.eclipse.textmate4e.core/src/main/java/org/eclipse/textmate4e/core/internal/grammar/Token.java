package org.eclipse.textmate4e.core.internal.grammar;

import java.util.List;

import org.eclipse.textmate4e.core.grammar.IToken;

class Token implements IToken {

	private int startIndex;

	private int endIndex;

	private List<String> scopes;

	public Token(int startIndex, int endIndex, List<String> scopes) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.scopes = scopes;
	}

	@Override
	public int getStartIndex() {
		return startIndex;
	}

	@Override
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	@Override
	public int getEndIndex() {
		return endIndex;
	}

	@Override
	public List<String> getScopes() {
		return scopes;
	}
}
