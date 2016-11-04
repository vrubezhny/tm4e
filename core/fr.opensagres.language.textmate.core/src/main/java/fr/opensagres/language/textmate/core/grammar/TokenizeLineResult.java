package fr.opensagres.language.textmate.core.grammar;

import java.util.List;

class TokenizeLineResult implements ITokenizeLineResult {

	private final IToken[] tokens;
	private final List<StackElement> ruleStack;

	public TokenizeLineResult(IToken[] tokens, List<StackElement> ruleStack) {
		this.tokens = tokens;
		this.ruleStack = ruleStack;
	}

	@Override
	public IToken[] getTokens() {
		return tokens;
	}

	@Override
	public List<StackElement> getRuleStack() {
		return ruleStack;
	}

}
