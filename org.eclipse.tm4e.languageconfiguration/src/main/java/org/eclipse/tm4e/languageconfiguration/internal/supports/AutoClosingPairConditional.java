package org.eclipse.tm4e.languageconfiguration.internal.supports;

import java.util.List;

@SuppressWarnings("serial")
public class AutoClosingPairConditional extends CharacterPair {

	private List<String> notIn;

	public AutoClosingPairConditional(String open, String close, List<String> notIn) {
		super(open, close);
		this.notIn = notIn;
	}

	public List<String> getNotIn() {
		return notIn;
	}
}
