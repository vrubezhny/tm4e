package org.eclipse.textmate4e.core.grammar;

import java.util.List;

public interface ITokenizeLineResult {

	IToken[] getTokens();

	List<StackElement> getRuleStack();

}
