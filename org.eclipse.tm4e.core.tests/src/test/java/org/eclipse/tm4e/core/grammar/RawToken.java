package org.eclipse.tm4e.core.grammar;

import java.util.List;

public class RawToken {

	private String value;
	private List<String> scopes;

	public RawToken() {
	}

	public RawToken(String value, List<String> scopes) {
		this.value = value;
		this.scopes = scopes;
	}

	public String getValue() {
		return value;
	}

	public List<String> getScopes() {
		return scopes;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof RawToken)) {
			return false;
		}
		RawToken other = (RawToken) obj;
		if (!(this.value.equals(other.value))) {
			return false;
		}
		return this.scopes.equals(other.scopes);
	}
}
