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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.textmate4e.ui.themes.ITheme;
import org.eclipse.textmate4e.ui.themes.ITokenProvider;
import org.eclipse.textmate4e.ui.themes.css.CSSTokenProvider;

/**
 * {@link ITheme} implementation.
 *
 */
public class Theme implements ITheme {

	private final IConfigurationElement ce;
	private String id;
	private String name;
	private String path;

	private ITokenProvider tokenProvider;

	public Theme(IConfigurationElement ce) {
		this.ce = ce;
		this.id = ce.getAttribute("id");
		this.name = ce.getAttribute("name");
		this.path = ce.getAttribute("path");
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
	public IToken getToken(String type) {
		return getTokenProvider().getToken(type);
	}

	private ITokenProvider getTokenProvider() {
		if (tokenProvider == null) {
			if (path != null && path.length() > 0) {
				String pluginId = ce.getNamespaceIdentifier();
				try {
					File bundleDir = FileLocator.getBundleFile(Platform.getBundle(pluginId));
					InputStream in = new FileInputStream(new File(bundleDir, path));
					tokenProvider = new CSSTokenProvider(in);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return tokenProvider;
	}

}
