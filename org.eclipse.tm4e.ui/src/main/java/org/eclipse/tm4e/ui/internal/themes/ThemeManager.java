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
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.tm4e.ui.TMUIPlugin;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.tm4e.ui.themes.IThemeManager;

/**
 * TextMate theme manager implementation.
 *
 */
public class ThemeManager implements IThemeManager, IRegistryChangeListener {

	// Theme for E4 CSS Engine
	private static final String E4_CSS_THEME_PREFERENCE_ID = "org.eclipse.e4.ui.css.swt.theme"; //$NON-NLS-1$
	public static final String E4_THEME_ID = "themeid"; //$NON-NLS-1$

	// "themes" extension point
	private static final String EXTENSION_THEMES = "themes"; //$NON-NLS-1$

	// "theme" declaration
	private static final String THEME_ELT = "theme"; //$NON-NLS-1$
	private static final String ECLIPSE_THEME_ID_ATTR = "eclipseThemeId"; //$NON-NLS-1$

	// "themeAssociation" declaration
	private static final String THEME_ASSOCIATION_ELT = "themeAssociation"; //$NON-NLS-1$
	private static final String THEME_ID_ATTR = "themeId"; //$NON-NLS-1$
	private static final String SCOPE_NAME_ATTR = "scopeName"; //$NON-NLS-1$

	private static final ThemeManager INSTANCE = new ThemeManager();

	public static ThemeManager getInstance() {
		return INSTANCE;
	}

	private Map<String /* theme id */ , ITheme> themes;
	private ThemeAssociationRegistry themeAssociationRegistry;

	private ThemeManager() {
	}

	@Override
	public ITheme getDefaultTheme() {
		String themeIdForE4Theme = getPreferenceE4CSSThemeId();
		return getThemeForScope(null, themeIdForE4Theme);
	}

	@Override
	public ITheme getThemeForScope(String scopeName, String eclipseThemeId) {
		loadThemesIfNeeded();
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
		return themeAssociationRegistry.getThemeAssociationsFor(scopeName);
	}

	@Override
	public ITheme[] getThemes() {
		loadThemesIfNeeded();
		Collection<ITheme> themes = this.themes.values();
		return themes.toArray(new ITheme[themes.size()]);
	}

	@Override
	public ITheme getThemeById(String themeId) {
		loadThemesIfNeeded();
		return themes.get(themeId);
	}

	@Override
	public void registryChanged(IRegistryChangeEvent event) {
		// TODO : implement that.
	}

	public void initialize() {

	}

	public void destroy() {
		Platform.getExtensionRegistry().removeRegistryChangeListener(this);
	}

	/**
	 * Load the themes.
	 */
	private void loadThemesIfNeeded() {
		if (themes != null) {
			// Themes are already loaded from plugin extension.
			return;
		}
		// Load themes from plugin extension.
		loadThemes();
	}

	private synchronized void loadThemes() {
		if (themes != null) {
			return;
		}
		IConfigurationElement[] cf = Platform.getExtensionRegistry().getConfigurationElementsFor(TMUIPlugin.PLUGIN_ID,
				EXTENSION_THEMES);
		Map<String, ITheme> themes = new HashMap<>();
		ThemeAssociationRegistry themeAssociationRegistry = new ThemeAssociationRegistry();
		Map<String, ITheme> defaultThemes = new HashMap<>();
		loadThemes(cf, themes, themeAssociationRegistry, defaultThemes);
		addRegistryListener();
		this.themeAssociationRegistry = themeAssociationRegistry;
		this.themes = themes;
	}

	private void addRegistryListener() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this, TMUIPlugin.PLUGIN_ID);
	}

	/**
	 * Load TextMate themes declared from the extension point.
	 */
	private void loadThemes(IConfigurationElement[] cf, Map<String, ITheme> themes, ThemeAssociationRegistry registry,
			Map<String, ITheme> defaultThemes) {
		for (IConfigurationElement ce : cf) {
			String name = ce.getName();
			if (THEME_ELT.equals(name)) {
				// theme
				Theme theme = new Theme(ce);
				themes.put(theme.getId(), theme);
			} else if (THEME_ASSOCIATION_ELT.equals(name)) {
				// themeAssociation
				String themeId = ce.getAttribute(THEME_ID_ATTR);
				String eclipseThemeId = ce.getAttribute(ECLIPSE_THEME_ID_ATTR);
				String scopeName = ce.getAttribute(SCOPE_NAME_ATTR);
				registry.register(new ThemeAssociation(themeId, eclipseThemeId, scopeName, this));
			}
		}
	}

	private String getPreferenceE4CSSThemeId() {
		IEclipsePreferences preferences = getPreferenceE4CSSTheme();
		return preferences != null ? preferences.get(E4_THEME_ID, null) : null;
	}

	public IEclipsePreferences getPreferenceE4CSSTheme() {
		return InstanceScope.INSTANCE.getNode(E4_CSS_THEME_PREFERENCE_ID);
	}

}
