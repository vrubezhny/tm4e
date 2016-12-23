package org.eclipse.tm4e.core.model;

import java.util.List;

public class LineTokens {

	List<TMToken> tokens;
	int actualStopOffset;
	TMState endState;

	public LineTokens(List<TMToken> tokens, int actualStopOffset, TMState endState) {
		this.tokens = tokens;
		this.actualStopOffset = actualStopOffset;
		this.endState = endState;
	}

	public TMState getEndState() {
		return endState;
	}

	public void setEndState(TMState endState) {
		this.endState = endState;
	}

	public List<TMToken> getTokens() {
		return tokens;
	}
}
