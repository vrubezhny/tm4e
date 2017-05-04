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
package org.eclipse.tm4e.ui.snippets;

import org.eclipse.tm4e.registry.ITMResource;

/**
 * 
 * Snippet API.
 *
 */
public interface ISnippet extends ITMResource {

	/**
	 * Returns the scope name of the snippet.
	 * 
	 * @return the scope name of the snippet.
	 */
	String getScopeName();

	/**
	 * Returns the content of the snippet.
	 * 
	 * @return the content of the snippet.
	 */
	String getContent();
}
