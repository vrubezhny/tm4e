/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.language.textmate.eclipse.grammars;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.language.textmate.core.grammar.IGrammar;

/**
 * 
 * TextMate Grammar registry manager API.
 *
 */
public interface IGrammarRegistryManager {

	/**
	 * Returns the {@link IGrammar} for the given content type.
	 * 
	 * @param contentType
	 *            the content type.
	 * @return the {@link IGrammar} for the given content type.
	 */
	IGrammar getGrammarFor(IContentType contentType);
}
