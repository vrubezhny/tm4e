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
package org.eclipse.tm4e.ui.internal.preferences;

import java.lang.reflect.Type;
import java.util.Collection;

import org.eclipse.tm4e.ui.internal.themes.Theme;
import org.eclipse.tm4e.ui.internal.themes.ThemeAssociation;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;

/**
 * Helper class load, save theme preferences with Json format.
 *
 */
public class PreferenceHelper {

	private static final Gson DEFAULT_GSON;

	static {
		DEFAULT_GSON = new GsonBuilder().registerTypeAdapter(ITheme.class, new InstanceCreator<Theme>() {
			@Override
			public Theme createInstance(Type type) {
				return new Theme();
			}
		}).registerTypeAdapter(IThemeAssociation.class, new InstanceCreator<ThemeAssociation>() {
			@Override
			public ThemeAssociation createInstance(Type type) {
				return new ThemeAssociation();
			}
		}).create();
	}

	public static ITheme[] loadThemes(String json) {
		return DEFAULT_GSON.fromJson(json, Theme[].class);
	}

	public static String toJsonThemes(Collection<ITheme> themes) {
		return DEFAULT_GSON.toJson(themes);
	}

	public static IThemeAssociation[] loadThemeAssociations(String json) {
		return DEFAULT_GSON.fromJson(json, ThemeAssociation[].class);
	}
	
	public static String toJsonThemeAssociations(Collection<IThemeAssociation> themeAssociations) {
		return DEFAULT_GSON.toJson(themeAssociations);
	}
}
