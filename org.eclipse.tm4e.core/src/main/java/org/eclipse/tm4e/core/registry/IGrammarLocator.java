package org.eclipse.tm4e.core.registry;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface IGrammarLocator {

	public static final IGrammarLocator DEFAULT_LOCATOR = new IGrammarLocator() {

		@Override
		public String getFilePath(String scopeName) {
			return null;
		}
		
		@Override
		public InputStream getInputStream(String scopeName) {
			return null;
		}

		@Override
		public Collection<String> getInjections(String scopeName) {
			return null;
		}
	};
	
	String getFilePath(String scopeName);

	InputStream getInputStream(String scopeName) throws IOException;
	
	Collection<String> getInjections(String scopeName);
	
}
