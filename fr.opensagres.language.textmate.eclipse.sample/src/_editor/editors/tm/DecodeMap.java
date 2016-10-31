package _editor.editors.tm;

import java.util.HashMap;
import java.util.Map;

public class DecodeMap {

	int lastAssignedId;
	Map<String /* scope */, int[] /* ids */ > scopeToTokenIds;
	Map<String /* token */, Integer /* id */ > tokenToTokenId;
	Map<Integer /* id */, String /* id */ > tokenIdToToken;
	TMTokenDecodeData prevToken;;

	public DecodeMap() {
		this.lastAssignedId = 0;
		this.scopeToTokenIds = new HashMap<>();
		this.tokenToTokenId = new HashMap<>();
		this.tokenIdToToken = new HashMap<>();
		this.prevToken = new TMTokenDecodeData(new String[0], new HashMap<Integer, Map<Integer, Boolean>>());
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
