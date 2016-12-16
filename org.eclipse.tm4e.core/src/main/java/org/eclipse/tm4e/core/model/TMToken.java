package org.eclipse.tm4e.core.model;

public class TMToken {

	public final int startIndex;
	public final String type;

	public TMToken(int startIndex, String type) {
		this.startIndex = startIndex;
		this.type = type;
	}
}
