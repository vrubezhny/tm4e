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
package org.eclipse.tm4e.ui.themes;

import org.eclipse.core.runtime.content.IContentType;

/**
 * TextMate theme manager API.
 *
 */
public interface IThemeManager {

	/**
	 * Returns the default theme.
	 * 
	 * @return the default theme.
	 */
	ITheme getDefaultTheme();

	/**
	 * Returns the {@link ITheme} for the given content type and the default
	 * theme otherwise.
	 * 
	 * @param contentTypes
	 *            the content type.
	 * @return the {@link ITheme} for the given content type and the default
	 *         theme otherwise.
	 */
	ITheme getThemeFor(IContentType[] contentTypes);

	/**
	 * Returns the {@link ITheme} by the theme id.
	 * 
	 * @param themeId
	 *            the theme id.
	 * @return the {@link ITheme} by the theme id.
	 */
	ITheme getThemeById(String themeId);

}
