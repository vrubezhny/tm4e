package org.eclipse.tm4e.core.model;

public class Range {

	public int fromLineNumber;
	public int toLineNumber;
	
	public Range(int fromLineNumber, int toLineNumber) {
		this.fromLineNumber = fromLineNumber;
		this.toLineNumber = toLineNumber;
	}
}
