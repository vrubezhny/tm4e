/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * This code is an translation of code copyrighted by Microsoft Corporation, and initially licensed under MIT.
 *
 * Contributors:
 *  - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.core.model;

import java.util.LinkedHashMap;
import java.util.Map;

class DecodeMap {

	int lastAssignedId;
	Map<String /* scope */, int[] /* ids */ > scopeToTokenIds;
	Map<String /* token */, Integer /* id */ > tokenToTokenId;
	Map<Integer /* id */, String /* id */ > tokenIdToToken;
	TMTokenDecodeData prevToken;;

	public DecodeMap() {
		this.lastAssignedId = 0;
		this.scopeToTokenIds = new LinkedHashMap<>();
		this.tokenToTokenId = new LinkedHashMap<>();
		this.tokenIdToToken = new LinkedHashMap<>();
		this.prevToken = new TMTokenDecodeData(new String[0], new LinkedHashMap<Integer, Map<Integer, Boolean>>());
	}

	public int[] getTokenIds(String scope) {
		int[] tokens = this.scopeToTokenIds.get(scope);
		if (tokens != null) {
			return tokens;
		}
		String[] tmpTokens = scope.split("[.]");

		tokens = new int[tmpTokens.length];
		for (int i = 0; i < tmpTokens.length; i++) {
			String token = tmpTokens[i];
			Integer tokenId = this.tokenToTokenId.get(token);
			if (tokenId == null) {
				tokenId = (++this.lastAssignedId);
				this.tokenToTokenId.put(token, tokenId);
				this.tokenIdToToken.put(tokenId, token);
			}
			tokens[i] = tokenId;
		}

		this.scopeToTokenIds.put(scope, tokens);
		return tokens;
	}

	public String getToken(Map<Integer, Boolean> tokenMap) {
		String result = "";
		boolean isFirst = true;
		for (int i = 1; i <= this.lastAssignedId; i++) {
			if (tokenMap.containsKey(i)) {
				if (isFirst) {
					isFirst = false;
					result += this.tokenIdToToken.get(i);
				} else {
					result += '.';
					result += this.tokenIdToToken.get(i);
				}
			}
		}
		return result;
	}
}
