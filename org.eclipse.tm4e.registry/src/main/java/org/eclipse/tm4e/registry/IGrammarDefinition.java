/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.registry;

import java.io.IOException;
import java.io.InputStream;

/**
 * TextMate grammar definition API.
 *
 */
public interface IGrammarDefinition {

	/**
	 * Returns the name of the TextMate grammar.
	 * 
	 * @return the name of the TextMate grammar.
	 */
	String getName();
	
	/**
	 * Returns the scope name of the TextMate grammar.
	 * 
	 * @return the scope name of the TextMate grammar.
	 */
	String getScopeName();

	/**
	 * Returns the TextMate grammar path.
	 * 
	 * @return the TextMate grammar path.
	 */
	String getPath();

	/**
	 * Returns the plugin id which has registered the TextMate grammar.
	 * 
	 * @return the plugin id which has registered the TextMate grammar.
	 */
	String getPluginId();

	/**
	 * Returns the stream of the TextMate grammar.
	 * 
	 * @return the stream of the TextMate grammar.
	 * @throws IOException
	 */
	public InputStream getInputStream() throws IOException;
}
