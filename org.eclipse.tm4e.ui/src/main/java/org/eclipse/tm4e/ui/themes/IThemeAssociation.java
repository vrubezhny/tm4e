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
 * Theme association API.
 *
 */
public interface IThemeAssociation {

	/**
	 * Returns the TextMate theme id.
	 * 
	 * @return the TextMate theme id.
	 */
	String getThemeId();

	/**
	 * Returns the E4 Theme id linked to the theme id and null otherwise.
	 * 
	 * @return the E4 Theme id linked to the theme id and null otherwise.
	 */
	String getEclipseThemeId();

	/**
	 * Returns the TextMate grammar scope linked to the theme id and null
	 * otherwise.
	 * 
	 * @return the TextMate grammar scope to the theme id and null otherwise.
	 */
	String getScopeName();

	/**
	 * Returns the TextMate theme.
	 * 
	 * @return the TextMate theme.
	 */
	ITheme getTheme();

}
