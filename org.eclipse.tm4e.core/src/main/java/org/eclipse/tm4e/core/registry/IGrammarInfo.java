package org.eclipse.tm4e.core.registry;

public interface IGrammarInfo {

	String[] getFileTypes();

	String getName();

	String getScopeName();

	String firstLineMatch();
}
