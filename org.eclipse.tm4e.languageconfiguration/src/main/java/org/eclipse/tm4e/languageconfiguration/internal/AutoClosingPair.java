package org.eclipse.tm4e.languageconfiguration.internal;

public class AutoClosingPair implements IAutoClosingPair {

	private String open;
	
	private String close;
	
	@Override
	public String getOpen() {
		return open;
	}
	
	@Override
	public String getClose() {
		return close;
	}
}
