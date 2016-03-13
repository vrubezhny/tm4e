package fr.opensagres.language.textmate.grammar;

import java.util.ArrayList;
import java.util.List;

class LineTokens {

	private final List<IToken> _tokens;
	private int _lastTokenEndIndex;

	LineTokens() {
		this._tokens = new ArrayList<IToken>();
		this._lastTokenEndIndex = 0;
	}

	public void produce(List<StackElement> stack, int endIndex) {
		produce(stack, endIndex, null);
	}

	public void produce(List<StackElement> stack, int endIndex, List<LocalStackElement> extraScopes) {
		// console.log('PRODUCE TOKEN: lastTokenEndIndex: ' + lastTokenEndIndex
		// + ', endIndex: ' + endIndex);
		if (this._lastTokenEndIndex >= endIndex) {
			return;
		}

		List<String> scopes = new ArrayList<String>();
		for (StackElement el : stack) {

			if (el.getScopeName() != null) {
				scopes.add(el.getScopeName());
			}

			if (el.getContentName() != null) {
				scopes.add(el.getContentName());
			}
		}

		if (extraScopes != null) {
			for (LocalStackElement extraScope : extraScopes) {
				scopes.add(extraScope.getScopeName());
			}
		}

		this._tokens.add(new Token(this._lastTokenEndIndex, endIndex, scopes));
		this._lastTokenEndIndex = endIndex;
	}

	public IToken[] getResult(List<StackElement> stack, int lineLength) {
		if (this._tokens.size() > 0 && this._tokens.get(this._tokens.size() - 1).getStartIndex() == lineLength - 1) {
			// pop produced token for newline
			this._tokens.remove(this._tokens.size() - 1);
		}

		if (this._tokens.size() == 0) {
			this._lastTokenEndIndex = -1;
			this.produce(stack, lineLength, null);
			this._tokens.get(this._tokens.size() - 1).setStartIndex(0);
		}

		return this._tokens.toArray(new IToken[0]);
	}
}
