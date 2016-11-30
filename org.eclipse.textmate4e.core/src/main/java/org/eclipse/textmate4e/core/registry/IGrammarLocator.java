package org.eclipse.textmate4e.core.registry;

import java.util.Collection;

public interface IGrammarLocator {

	String getFilePath(String scopeName);

	Collection<String> getInjections(String scopeName);
	
}
