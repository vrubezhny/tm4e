package org.eclipse.language.textmate.core.registry;

public interface IGrammarInfo {

	String[] getFileTypes();

	String getName();

	String getScopeName();

	String firstLineMatch();
}
