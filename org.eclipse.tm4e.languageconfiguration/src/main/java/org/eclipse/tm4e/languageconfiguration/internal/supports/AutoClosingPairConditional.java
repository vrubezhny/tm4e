package org.eclipse.tm4e.languageconfiguration.internal.supports;

import java.util.List;

public class AutoClosingPairConditional extends AutoClosingPair {

	private List<String> notIn;

	public AutoClosingPairConditional(String open, String close) {
		super(open, close);
	}

	public List<String> getNotIn() {
		return notIn;
	}
}
