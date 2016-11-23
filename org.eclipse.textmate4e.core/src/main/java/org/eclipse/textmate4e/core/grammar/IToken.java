package org.eclipse.textmate4e.core.grammar;

import java.util.List;

public interface IToken {

	int getStartIndex();

	void setStartIndex(int startIndex);

	int getEndIndex();

	List<String> getScopes();

}
