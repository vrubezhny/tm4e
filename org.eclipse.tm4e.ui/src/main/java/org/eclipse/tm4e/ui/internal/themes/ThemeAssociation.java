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
package org.eclipse.tm4e.ui.internal.themes;

import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.tm4e.ui.themes.IThemeManager;

/**
 * Theme association implementation.
 *
 */
public class ThemeAssociation implements IThemeAssociation {

	private final String themeId;
	private final String eclipseThemeId;
	private final String scopeName;
	private final boolean defaultAssociation;
	private final IThemeManager themeManager;

	public ThemeAssociation(String themeId, String eclipseThemeId, String scopeName, boolean defaultAssociation,
			IThemeManager themeManager) {
		this.themeId = themeId;
		this.eclipseThemeId = eclipseThemeId;
		this.scopeName = scopeName;
		this.themeManager = themeManager;
		this.defaultAssociation = defaultAssociation;
	}

	@Override
	public String getThemeId() {
		return themeId;
	}

	@Override
	public String getEclipseThemeId() {
		return eclipseThemeId;
	}

	@Override
	public String getScopeName() {
		return scopeName;
	}

	@Override
	public ITheme getTheme() {
		return themeManager.getThemeById(getThemeId());
	}

	@Override
	public boolean isDefault() {
		return defaultAssociation;
	}
}
