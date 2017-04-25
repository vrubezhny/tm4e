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

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.ITokenProvider;
import org.eclipse.tm4e.ui.themes.css.CSSTokenProvider;

/**
 * {@link ITheme} implementation.
 *
 */
public class Theme implements ITheme {

	private static final String PLATFORM_PLUGIN = "platform:/plugin/"; //$NON-NLS-1$

	private final IConfigurationElement ce;
	private final String id;
	private final String name;
	private final String path;
	private final String pluginId;

	private ITokenProvider tokenProvider;

	public Theme(IConfigurationElement ce) {
		this.ce = ce;
		this.id = ce.getAttribute("id");
		this.name = ce.getAttribute("name");
		this.path = ce.getAttribute("path");
		this.pluginId = ce.getNamespaceIdentifier();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPath() {
		return path;
	}

	public String getPluginId() {
		return pluginId;
	}

	@Override
	public IToken getToken(String type) {
		return getTokenProvider().getToken(type);
	}

	private ITokenProvider getTokenProvider() {
		if (tokenProvider == null) {
			if (path != null && path.length() > 0) {
				String pluginId = ce.getNamespaceIdentifier();
				try {
					URL url = new URL(
							new StringBuilder(PLATFORM_PLUGIN).append(pluginId).append("/").append(path).toString());
					tokenProvider = new CSSTokenProvider(url.openStream());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
		return tokenProvider;
	}

	@Override
	public String toCSSStyleSheet() {
		String pluginId = ce.getNamespaceIdentifier();
		try {
			URL url = new URL(new StringBuilder(PLATFORM_PLUGIN).append(pluginId).append("/").append(path).toString());
			return convertStreamToString(url.openStream());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
