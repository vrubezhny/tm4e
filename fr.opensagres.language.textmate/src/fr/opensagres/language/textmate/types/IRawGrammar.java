package fr.opensagres.language.textmate.types;

public interface IRawGrammar {

	IRawRepository getRepository();

	String getScopeName();

	IRawRule[] getPatterns();

	// injections?:{ [expression:string]: IRawRule };

	String[] getFileTypes();

	String getName();

	String getFirstLineMatch();
}
