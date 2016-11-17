package org.eclipse.language.textmate.core.grammar;

import java.util.List;

public interface IToken {

	int getStartIndex();

	void setStartIndex(int startIndex);

	int getEndIndex();

	List<String> getScopes();

}
