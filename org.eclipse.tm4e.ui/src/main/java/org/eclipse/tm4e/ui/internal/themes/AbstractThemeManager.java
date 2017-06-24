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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.tm4e.ui.themes.IThemeManager;

/**
 * TextMate theme manager implementation.
 *
 */
public abstract class AbstractThemeManager implements IThemeManager {

	// Theme for E4 CSS Engine
	private static final String E4_CSS_THEME_PREFERENCE_ID = "org.eclipse.e4.ui.css.swt.theme"; //$NON-NLS-1$
	public static final String E4_THEME_ID = "themeid"; //$NON-NLS-1$

	private final Map<String /* theme id */ , ITheme> themes;
	private final ThemeAssociationRegistry themeAssociationRegistry;

	public AbstractThemeManager() {
		this.themes = new HashMap<>();
		this.themeAssociationRegistry = new ThemeAssociationRegistry();
	}

	@Override
	public ITheme getDefaultTheme() {
		String themeIdForE4Theme = getPreferenceE4CSSThemeId();
		return getThemeForScope(null, themeIdForE4Theme);
	}

	@Override
	public ITheme getThemeForScope(String scopeName, String eclipseThemeId) {
		IThemeAssociation association = themeAssociationRegistry.getThemeAssociationFor(scopeName, eclipseThemeId);
		String themeId = association.getThemeId();
		return getThemeById(themeId);
	}

	@Override
	public ITheme getThemeForScope(String scopeName) {
		return getThemeForScope(scopeName, getPreferenceE4CSSThemeId());
	}

	@Override
	public IThemeAssociation[] getThemeAssociationsForScope(String scopeName) {
		return themeAssociationRegistry.getThemeAssociationsForScope(scopeName);
	}

	@Override
	public IThemeAssociation[] getThemeAssociationsForTheme(String themeId) {
		return themeAssociationRegistry.getThemeAssociationsForTheme(themeId);
	}

	@Override
	public ITheme[] getThemes() {
		Collection<ITheme> themes = this.themes.values();
		return themes.toArray(new ITheme[themes.size()]);
	}

	@Override
	public ITheme getThemeById(String themeId) {
		return themes.get(themeId);
	}

	private String getPreferenceE4CSSThemeId() {
		IEclipsePreferences preferences = getPreferenceE4CSSTheme();
		return preferences != null ? preferences.get(E4_THEME_ID, null) : null;
	}

	protected IEclipsePreferences getPreferenceE4CSSTheme() {
		return InstanceScope.INSTANCE.getNode(E4_CSS_THEME_PREFERENCE_ID);
	}

	@Override
	public void registerThemeAssociation(IThemeAssociation association) {
		themeAssociationRegistry.register(association);
	}

	@Override
	public void unregisterThemeAssociation(IThemeAssociation association) {
		themeAssociationRegistry.unregister(association);
	}
	
	@Override
	public List<IThemeAssociation> getThemeAssociations() {
		return themeAssociationRegistry.getThemeAssociations();
	}

	@Override
	public void registerTheme(ITheme theme) {
		themes.put(theme.getId(), theme);
	}

	@Override
	public void unregisterTheme(ITheme theme) {
		themes.remove(theme.getId());
	}
}
