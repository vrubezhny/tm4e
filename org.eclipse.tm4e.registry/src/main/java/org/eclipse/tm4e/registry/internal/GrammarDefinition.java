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
package org.eclipse.tm4e.registry.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tm4e.registry.IGrammarDefinition;

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

	private final String name;
	private final String path;
	private final String scopeName;
	private final String pluginId;

	public GrammarDefinition(IConfigurationElement element) {
		this.path = element.getAttribute(XMLConstants.PATH_ATTR);
		this.scopeName = element.getAttribute(XMLConstants.SCOPE_NAME_ATTR);
		this.name = getName(element.getAttribute(XMLConstants.NAME_ATTR), scopeName);
		this.pluginId = element.getNamespaceIdentifier();
	}

	private String getName(String name, String scopeName) {
		if (name != null && name.length() > 0) {
			return name;
		}
		return scopeName;
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
		if (path != null && path.length() > 0) {
			URL url = new URL(new StringBuilder(PLATFORM_PLUGIN).append(pluginId).append("/").append(path).toString());
			return url.openStream();
		}
		return null;
	}

}
