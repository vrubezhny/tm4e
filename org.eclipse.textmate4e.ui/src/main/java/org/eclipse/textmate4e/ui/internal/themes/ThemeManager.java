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
package org.eclipse.textmate4e.ui.internal.themes;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.textmate4e.ui.TMUIPlugin;
import org.eclipse.textmate4e.ui.themes.ITheme;
import org.eclipse.textmate4e.ui.themes.IThemeManager;

/**
 * TextMate theme manager implementation.
 *
 */
public class ThemeManager implements IThemeManager, IRegistryChangeListener {

	// "themes" extension point
	private static final String EXTENSION_THEMES = "themes";

	// "theme" declaration
	private static final String THEME_ELT_NAME = "theme";

	// "themeContentTypeBinding" declaration
	private static final String THEME_CONTENT_TYPE_BINDING_ELT_NAME = "themeContentTypeBinding";
	private static final String THEME_ID_ATTR_NAME = "themeId";
	private static final String CONTENT_TYPE_ID_ATTR_NAME = "contentTypeId";

	private static final String DEFAULT_THEME_ID = "org.eclipse.textmate4e.ui.themes.SolarizedLight";

	private static final ThemeManager INSTANCE = new ThemeManager();

	public static ThemeManager getInstance() {
		return INSTANCE;
	}

	private Map<String /* theme id */ , Theme> themes;
	private Map<String /* content type id */, String /* theme id */> themeContentTypeBindings;

	private ThemeManager() {
	}

	@Override
	public ITheme getDefaultTheme() {
		loadThemesIfNeeded();
		return themes.get(DEFAULT_THEME_ID);
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
		String themeId = null;
		for (IContentType contentType : contentTypes) {
			themeId = themeContentTypeBindings.get(contentType.getId());
			if (themeId != null) {
				return themeId;
			}
		}
		return null;
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
		loadThemes(cf, themes, themeContentTypeBindings);
		addRegistryListener();
		this.themeContentTypeBindings = themeContentTypeBindings;
		this.themes = themes;
	}

	private void addRegistryListener() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this, TMUIPlugin.PLUGIN_ID);
	}

	/**
	 * Load TextMate themes declared from the extension point.
	 */
	private void loadThemes(IConfigurationElement[] cf, Map<String, Theme> themes,
			Map<String, String> themeContentTypeBindings) {
		for (IConfigurationElement ce : cf) {
			String name = ce.getName();
			if (THEME_ELT_NAME.equals(name)) {
				// theme
				Theme theme = new Theme(ce);
				themes.put(theme.getId(), theme);
			} else if (THEME_CONTENT_TYPE_BINDING_ELT_NAME.equals(name)) {
				// themeContentTypeBinding
				String contentTypeId = ce.getAttribute(CONTENT_TYPE_ID_ATTR_NAME);
				String themeId = ce.getAttribute(THEME_ID_ATTR_NAME);
				themeContentTypeBindings.put(contentTypeId, themeId);
			}
		}
	}

}
