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
import java.util.Scanner;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.tm4e.registry.TMResource;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.ITokenProvider;
import org.eclipse.tm4e.ui.themes.css.CSSTokenProvider;

/**
 * {@link ITheme} implementation.
 *
 */
public class Theme extends TMResource implements ITheme {

	private ITokenProvider tokenProvider;

	private String id;

	/**
	 * Constructor for user preferences (loaded from Json with Gson).
	 */
	public Theme() {
		super();
	}

	/**
	 * Constructor for extension point.
	 * 
	 * @param element
	 */
	public Theme(String id, String path, String name) {
		super(path, name);
		this.id = id;
	}

	public Theme(IConfigurationElement ce) {
		super(ce);
		id = ce.getAttribute("id");
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public IToken getToken(String type) {
		return getTokenProvider().getToken(type);
	}

	private ITokenProvider getTokenProvider() {
		if (tokenProvider == null) {
			try {
				InputStream in = super.getInputStream();
				if (in == null) {
					return null;
				}
				tokenProvider = new CSSTokenProvider(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tokenProvider;
	}

	@Override
	public String toCSSStyleSheet() {
		return super.getResourceContent();
	}

	private static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

}
