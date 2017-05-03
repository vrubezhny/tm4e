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
package org.eclipse.tm4e.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tm4e.registry.internal.XMLConstants;

/**
 * Grammar definition defined by the "org.eclipse.tm4e.registry.grammars"
 * extension point. Here a sample to register TypeScript TextMate grammar.
 * 
 * <pre>
 * <extension
         point="org.eclipse.tm4e.registry.grammars">
      <grammar
      		scopeName="source.ts"
            path="./syntaxes/TypeScript.tmLanguage.json" >
      </grammar>
   </extension>
 * </pre>
 *
 */
public class GrammarDefinition implements IGrammarDefinition {

	private static final String PLATFORM_PLUGIN = "platform:/plugin/"; //$NON-NLS-1$

	private String name;
	private String path;
	private String scopeName;
	private String pluginId;

	/**
	 * Constructor for user preferences (loaded from Json with Gson).
	 */
	public GrammarDefinition() {

	}

	/**
	 * Constructor for extension point.
	 * 
	 * @param element
	 */
	public GrammarDefinition(String scopeName, String path, String name) {
		this.scopeName = scopeName;
		this.path = path;
		this.name = name;
	}

	public GrammarDefinition(IConfigurationElement ce) {
		this(ce.getAttribute(XMLConstants.SCOPE_NAME_ATTR), ce.getAttribute(XMLConstants.PATH_ATTR),
				ce.getAttribute(XMLConstants.SCOPE_NAME_ATTR));
		this.pluginId = ce.getNamespaceIdentifier();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getScopeName() {
		return scopeName;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getPluginId() {
		return pluginId;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (path == null || path.length() < 0) {
			return null;
		}
		if (pluginId != null) {
			URL url = new URL(new StringBuilder(PLATFORM_PLUGIN).append(pluginId).append("/").append(path).toString());
			return url.openStream();
		}
		return new FileInputStream(new File(path));
	}

}
