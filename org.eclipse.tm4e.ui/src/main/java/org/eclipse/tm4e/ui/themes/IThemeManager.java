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
	 * Returns the TextMate theme {@link ITheme} for the given TextMate grammar
	 * <code>scopeName</code> and E4 Theme <code>eclipseThemeId</code>.
	 * 
	 * @param scopeName
	 *            the TextMate grammar
	 * @param eclipseThemeId
	 *            the E4 Theme.
	 * @return the TextMate theme {@link ITheme} for the given TextMate grammar
	 *         <code>scopeName</code> and E4 Theme <code>eclipseThemeId</code>.
	 */
	ITheme getThemeForScope(String scopeName, String eclipseThemeId);

	/**
	 * Returns the TextMate theme {@link ITheme} for the given TextMate grammar
	 * <code>scopeName</code> and default E4 Theme.
	 * 
	 * @param scopeName
	 * @return the TextMate theme {@link ITheme} for the given TextMate grammar
	 *         <code>scopeName</code> and default E4 Theme.
	 */
	ITheme getThemeForScope(String scopeName);

	/**
	 * Returns the theme associations for the given TextMate grammar
	 * <code>scopeName</code>.
	 * 
	 * @param scopeName
	 * @return the theme associations for the given TextMate grammar
	 *         <code>scopeName</code>.
	 */
	IThemeAssociation[] getThemeAssociationsForScope(String scopeName);

	/**
	 * Returns the theme associations for the given TextMate theme
	 * <code>themeId</code>.
	 * 
	 * @param themeId
	 * @return the theme associations for the given TextMate theme
	 *         <code>themeId</code>.
	 */
	IThemeAssociation[] getThemeAssociationsForTheme(String themeId);

	/**
	 * Returns the {@link ITheme} by the theme id.
	 * 
	 * @param themeId
	 *            the theme id.
	 * @return the {@link ITheme} by the theme id.
	 */
	ITheme getThemeById(String themeId);

	/**
	 * Returns the list of TextMate themes.
	 * 
	 * @return the list of TextMate themes.
	 */
	ITheme[] getThemes();

}
