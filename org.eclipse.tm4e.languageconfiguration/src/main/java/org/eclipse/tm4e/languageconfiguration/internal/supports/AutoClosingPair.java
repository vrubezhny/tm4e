package org.eclipse.tm4e.languageconfiguration.internal.supports;

public class AutoClosingPair {

	private final String open;

	private final String close;

	public AutoClosingPair(String open, String close) {
		this.open = open;
		this.close = close;
	}

	public String getOpen() {
		return open;
	}

	public String getClose() {
		return close;
	}
}
