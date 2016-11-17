package org.eclipse.language.textmate.eclipse.internal.themes;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.language.textmate.eclipse.TMPlugin;
import org.eclipse.language.textmate.eclipse.themes.IThemeManager;
import org.eclipse.language.textmate.eclipse.themes.ITokenProvider;

public class ThemeManager implements IThemeManager, IRegistryChangeListener {

	private static final ThemeManager INSTANCE = new ThemeManager();
	private static final String EXTENSION_THEMES = "themes";

	public static ThemeManager getInstance() {
		return INSTANCE;
	}

	private boolean registryListenerIntialized;
	private Map<String, Theme> themes;
	private Map<String, String> themeContentTypeBindings;

	public ThemeManager() {

	}

	@Override
	public ITokenProvider getThemeFor(String contentTypeId) {
		loadThemesIfNeeded();
		String themeId = themeContentTypeBindings.get(contentTypeId);
		if (themeId != null) {
			ITokenProvider tokenProvider = getThemeById(themeId);
			if (tokenProvider != null) {
				return tokenProvider;
			}
		}
		for (Theme theme : themes.values()) {
			return theme.getTokenProvider();
		}
		return null;
	}

	@Override
	public ITokenProvider getThemeById(String themeId) {
		loadThemesIfNeeded();
		Theme theme = themes.get(themeId);
		return theme != null ? theme.getTokenProvider() : null;
	}

	@Override
	public void registryChanged(IRegistryChangeEvent event) {

	}

	public void initialize() {

	}

	public void destroy() {
		Platform.getExtensionRegistry().removeRegistryChangeListener(this);
	}

	/**
	 * Load the theme.
	 */
	private void loadThemesIfNeeded() {
		if (themes != null) {
			return;
		}
		loadThemes();
	}

	private synchronized void loadThemes() {
		if (themes != null) {
			return;
		}
		IConfigurationElement[] cf = Platform.getExtensionRegistry().getConfigurationElementsFor(TMPlugin.PLUGIN_ID,
				EXTENSION_THEMES);
		Map<String, Theme> themes = new HashMap<>();
		Map<String, String> themeContentTypeBindings = new HashMap<>();
		loadThemes(cf, themes, themeContentTypeBindings);
		addRegistryListener();
		this.themeContentTypeBindings = themeContentTypeBindings;
		this.themes = themes;
	}

	private void addRegistryListener() {
		if (registryListenerIntialized)
			return;

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this, TMPlugin.PLUGIN_ID);
		registryListenerIntialized = true;
	}

	/**
	 * Load TextMate themes declared from the extension point.
	 */
	private void loadThemes(IConfigurationElement[] cf, Map<String, Theme> themes,
			Map<String, String> themeContentTypeBindings) {
		for (IConfigurationElement ce : cf) {
			String name = ce.getName();
			if ("theme".equals(name)) {
				Theme theme = new Theme(ce);
				themes.put(theme.getId(), theme);
			} else if ("themeContentTypeBinding".equals(name)) {
				String contentTypeId = ce.getAttribute("contentTypeId");
				String themeId = ce.getAttribute("themeId");
				themeContentTypeBindings.put(contentTypeId, themeId);
			}

		}
	}

}
