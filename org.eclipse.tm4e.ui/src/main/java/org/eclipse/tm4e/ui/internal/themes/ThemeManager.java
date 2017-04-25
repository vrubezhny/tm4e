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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.tm4e.ui.TMUIPlugin;
import org.eclipse.tm4e.ui.themes.ITheme;
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
	private static final String THEME_ELT_NAME = "theme"; //$NON-NLS-1$
	private static final String DEFAULT_ECLIPSE_THEME_ID = "eclipseThemeId"; //$NON-NLS-1$
	private static final String DEFAULT_E4_THEME_ID = "*";

	// "themeContentTypeBinding" declaration
	private static final String THEME_CONTENT_TYPE_BINDING_ELT_NAME = "themeContentTypeBinding"; //$NON-NLS-1$
	private static final String THEME_ID_ATTR_NAME = "themeId"; //$NON-NLS-1$
	private static final String CONTENT_TYPE_ID_ATTR_NAME = "contentTypeId"; //$NON-NLS-1$

	private static final ThemeManager INSTANCE = new ThemeManager();

	public static ThemeManager getInstance() {
		return INSTANCE;
	}

	private Map<String /* theme id */ , Theme> themes;
	private Map<String /* content type id */, String /* theme id */> themeContentTypeBindings;
	private Map<String /* E4 theme id */ , Theme> defaultThemes;

	private ThemeManager() {
	}

	@Override
	public ITheme getDefaultTheme() {
		String themeIdForE4Theme = getPreferenceE4CSSThemeId();
		return getThemeForE4Theme(themeIdForE4Theme);
	}

	public ITheme getThemeForE4Theme(String e4ThemeId) {
		loadThemesIfNeeded();
		Theme themeForE4Theme = null;
		if (e4ThemeId != null) {
			themeForE4Theme = defaultThemes.get(e4ThemeId);
		}
		return themeForE4Theme != null ? themeForE4Theme : defaultThemes.get(DEFAULT_E4_THEME_ID);
	}

	@Override
	public ITheme getThemeFor(IContentType[] contentTypes) {
		loadThemesIfNeeded();
		String themeId = getThemeIdFor(contentTypes);
		if (themeId != null) {
			ITheme theme = getThemeById(themeId);
			if (theme != null) {
				return theme;
			}
		}
		return getDefaultTheme();
	}

	private String getThemeIdFor(IContentType[] contentTypes) {
		if (contentTypes == null) {
			return null;
		}
		String themeId = null;
		for (IContentType contentType : contentTypes) {
			themeId = getThemeIdFor(contentType.getId());
			if (themeId != null) {
				return themeId;
			}
		}
		return null;
	}

	@Override
	public ITheme getThemeFor(String contentTypeId) {
		loadThemesIfNeeded();
		String themeId = getThemeIdFor(contentTypeId);
		if (themeId != null) {
			ITheme theme = getThemeById(themeId);
			if (theme != null) {
				return theme;
			}
		}
		return getDefaultTheme();
	}

	/**
	 * 
	 * @param contentTypeId
	 * @return
	 */
	private String getThemeIdFor(String contentTypeId) {
		return themeContentTypeBindings.get(contentTypeId);
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
		Map<String, Theme> themes = new HashMap<>();
		Map<String, String> themeContentTypeBindings = new HashMap<>();
		Map<String, Theme> defaultThemes = new HashMap<>();
		loadThemes(cf, themes, themeContentTypeBindings, defaultThemes);
		addRegistryListener();
		this.themeContentTypeBindings = themeContentTypeBindings;
		this.themes = themes;
		this.defaultThemes = defaultThemes;
	}

	private void addRegistryListener() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this, TMUIPlugin.PLUGIN_ID);
	}

	/**
	 * Load TextMate themes declared from the extension point.
	 */
	private void loadThemes(IConfigurationElement[] cf, Map<String, Theme> themes,
			Map<String, String> themeContentTypeBindings, Map<String, Theme> defaultThemes) {
		for (IConfigurationElement ce : cf) {
			String name = ce.getName();
			if (THEME_ELT_NAME.equals(name)) {
				// theme
				Theme theme = new Theme(ce);
				themes.put(theme.getId(), theme);
				// Default theme for E4 theme
				String eclipseThemeId = ce.getAttribute(DEFAULT_ECLIPSE_THEME_ID);
				if (eclipseThemeId != null && eclipseThemeId.length() > 0) {
					defaultThemes.put(eclipseThemeId, theme);
				}
			} else if (THEME_CONTENT_TYPE_BINDING_ELT_NAME.equals(name)) {
				// themeContentTypeBinding
				String contentTypeId = ce.getAttribute(CONTENT_TYPE_ID_ATTR_NAME);
				String themeId = ce.getAttribute(THEME_ID_ATTR_NAME);
				themeContentTypeBindings.put(contentTypeId, themeId);
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
