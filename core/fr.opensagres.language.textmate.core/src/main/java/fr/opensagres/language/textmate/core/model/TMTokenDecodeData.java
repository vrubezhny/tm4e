package fr.opensagres.language.textmate.core.model;

import java.util.Map;

public class TMTokenDecodeData {

	public String[] scopes;
	public Map<Integer, Map<Integer, Boolean>> scopeTokensMaps;

	TMTokenDecodeData(String[] scopes, Map<Integer, Map<Integer, Boolean>> scopeTokensMaps) {
		this.scopes = scopes;
		this.scopeTokensMaps = scopeTokensMaps;
	}

}
