package org.eclipse.tm4e.languageconfiguration.internal.supports;

import java.util.List;

public class StandardAutoClosingPairConditional extends AutoClosingPair {

	private List<String> notIn;

	public StandardAutoClosingPairConditional(String open, String close, List<String> notIn) {
		super(open, close);
		this.notIn = notIn;
	}

	public List<String> getNotIn() {
		return notIn;
	}

}
