package fr.opensagres.language.textmate.types;

import java.util.Collection;

public interface IRawGrammar {

	IRawRepository getRepository();

	String getScopeName();

	Collection<IRawRule> getPatterns();

	// injections?:{ [expression:string]: IRawRule };

	String[] getFileTypes();

	String getName();

	String getFirstLineMatch();
}
