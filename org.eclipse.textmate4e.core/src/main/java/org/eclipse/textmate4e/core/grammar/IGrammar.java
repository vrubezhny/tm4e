package org.eclipse.textmate4e.core.grammar;

import java.util.List;

public interface IGrammar {

	ITokenizeLineResult tokenizeLine(String lineText);

	ITokenizeLineResult tokenizeLine(String lineText, List<StackElement> prevState);

}
