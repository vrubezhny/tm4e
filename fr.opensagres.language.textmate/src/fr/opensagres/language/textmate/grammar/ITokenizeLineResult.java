package fr.opensagres.language.textmate.grammar;

import java.util.List;

public interface ITokenizeLineResult {

	IToken[] getTokens();

	List<StackElement> getRuleStack();

}
