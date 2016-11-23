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
package org.eclipse.textmate4e.ui.themes;

import org.eclipse.jface.text.rules.IToken;

/**
 * Provider to retrieve Eclipse {@link IToken} from the TextMate token type.
 *
 */
public interface ITokenProvider {

	/**
	 * Returns the Eclipse {@link IToken} from the given type and null
	 * otherwise.
	 * 
	 * @param type
	 * @return the Eclipse {@link IToken} from the given type and null
	 *         otherwise.
	 */
	IToken getToken(String type);
}
