package org.eclipse.tm4e.languageconfiguration.internal;

public class StandardAutoClosingPairConditional implements IAutoClosingPair {

	private String open;
	private String close;

	public StandardAutoClosingPairConditional(AutoClosingPairConditional el) {
		this.open = el.getOpen();
		this.close = el.getClose();
	}

	@Override
	public String getOpen() {
		return open;
	}

	@Override
	public String getClose() {
		return close;
	}

}
