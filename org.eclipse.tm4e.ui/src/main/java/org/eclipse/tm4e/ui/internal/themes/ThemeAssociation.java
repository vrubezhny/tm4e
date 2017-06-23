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
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;

/**
 * Theme association implementation.
 *
 */
public class ThemeAssociation implements IThemeAssociation {

	private static final String ECLIPSE_THEME_ID_ATTR = "eclipseThemeId"; //$NON-NLS-1$
	private static final String THEME_ID_ATTR = "themeId"; //$NON-NLS-1$
	private static final String SCOPE_NAME_ATTR = "scopeName"; //$NON-NLS-1$
	private static final String DEFAULT_ATTR = "default"; //$NON-NLS-1$

	private String themeId;
	private String eclipseThemeId;
	private String scopeName;
	private boolean defaultAssociation;
	private String pluginId;

	/**
	 * Constructor for user preferences (loaded from Json with Gson).
	 */
	public ThemeAssociation() {
		super();
	}

	public ThemeAssociation(String themeId, String eclipseThemeId, String scopeName, boolean defaultAssociation) {
		this.themeId = themeId;
		this.eclipseThemeId = eclipseThemeId;
		this.scopeName = scopeName;
		this.defaultAssociation = defaultAssociation;
	}

	public ThemeAssociation(IConfigurationElement ce) {
		this(ce.getAttribute(THEME_ID_ATTR), ce.getAttribute(ECLIPSE_THEME_ID_ATTR), ce.getAttribute(SCOPE_NAME_ATTR),
				"true".equals(ce.getAttribute(DEFAULT_ATTR)));
		this.pluginId = ce.getNamespaceIdentifier();
	}

	@Override
	public String getPluginId() {
		return pluginId;
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
	public boolean isDefault() {
		return defaultAssociation;
	}
}
