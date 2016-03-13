package fr.opensagres.language.textmate.registry;

public interface IGrammarInfo {

	String[] getFileTypes();

	String getName();

	String getScopeName();

	String firstLineMatch();
}
