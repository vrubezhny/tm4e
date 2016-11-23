package org.eclipse.textmate4e.core.internal.types;

import java.util.Collection;

public interface IRawGrammar {

	IRawRepository getRepository();

	String getScopeName();

	Collection<IRawRule> getPatterns();

	// injections?:{ [expression:string]: IRawRule };

	Collection<String> getFileTypes();

	String getName();

	String getFirstLineMatch();
}
