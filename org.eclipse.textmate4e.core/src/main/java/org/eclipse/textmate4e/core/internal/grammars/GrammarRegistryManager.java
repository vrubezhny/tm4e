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
package org.eclipse.textmate4e.core.internal.grammars;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.textmate4e.core.TMCorePlugin;
import org.eclipse.textmate4e.core.grammar.IGrammar;
import org.eclipse.textmate4e.core.grammars.IGrammarRegistryManager;
import org.eclipse.textmate4e.core.registry.IGrammarLocator;

/**
 * 
 * TextMate Grammar registry manager implementation.
 *
 */
public class GrammarRegistryManager implements IGrammarRegistryManager, IRegistryChangeListener {

	private static final String EXTENSION_GRAMMARS = "grammars";

	private static final GrammarRegistryManager INSTANCE = new GrammarRegistryManager();

	public static GrammarRegistryManager getInstance() {
		return INSTANCE;
	}

	private boolean registryListenerIntialized;

	private GrammarRegistry registry;
	private Map<String, List<String>> injections;

	private Map<String, String> scopeNameBindings;

	public GrammarRegistryManager() {
		this.registryListenerIntialized = false;
		this.scopeNameBindings = new HashMap<>();
		injections = new HashMap<>();
	}

	@Override
	public IGrammar getGrammarFor(IContentType[] contentTypes) {
		loadGrammarsIfNeeded();
		// Find grammar by content type
		for (IContentType contentType : contentTypes) {
			String scopeName = getScopeName(contentType);
			if (scopeName != null) {
				IGrammar grammar = registry.getGrammar(scopeName);
				if (grammar != null) {
					return grammar;
				}
			}
		}
		return null;
	}

	private String getScopeName(IContentType contentType) {
		return scopeNameBindings.get(contentType.getId());
	}

	@Override
	public void registryChanged(final IRegistryChangeEvent event) {
		IExtensionDelta[] deltas = event.getExtensionDeltas(TMCorePlugin.PLUGIN_ID, EXTENSION_GRAMMARS);
		if (deltas != null) {
			for (IExtensionDelta delta : deltas)
				handleGrammarDelta(delta);
		}
	}

	private void handleGrammarDelta(IExtensionDelta delta) {
		// TODO Auto-generated method stub

	}

	public IGrammar registerGrammar(InputStream in) {
		return null;
	}

	public IGrammar registerGrammar(IPath grammarPath) {
		return null;
	}

	public void initialize() {

	}

	public void destroy() {
		Platform.getExtensionRegistry().removeRegistryChangeListener(this);
	}

	/**
	 * Load the grammar.
	 */
	private void loadGrammarsIfNeeded() {
		if (registry != null) {
			return;
		}
		loadGrammars();
	}

	private synchronized void loadGrammars() {
		if (registry != null) {
			return;
		}
		IConfigurationElement[] cf = Platform.getExtensionRegistry().getConfigurationElementsFor(TMCorePlugin.PLUGIN_ID,
				EXTENSION_GRAMMARS);
		GrammarRegistry registry = new GrammarRegistry(new IGrammarLocator() {

			@Override
			public Collection<String> getInjections(String scopeName) {
				return injections.get(scopeName);
			}

			@Override
			public String getFilePath(String scopeName) {
				GrammarDefinition info = GrammarRegistryManager.this.registry.getDefinition(scopeName);
				return info != null ? info.getPath() : null;
			}
			
			@Override
			public InputStream getInputStream(String scopeName) throws IOException {
				GrammarDefinition info = GrammarRegistryManager.this.registry.getDefinition(scopeName);
				return info != null ? info.getInputStream() : null;
			}
			
		});
		loadGrammars(cf, registry);
		addRegistryListener();
		this.registry = registry;
	}

	private void addRegistryListener() {
		if (registryListenerIntialized)
			return;

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this, TMCorePlugin.PLUGIN_ID);
		registryListenerIntialized = true;
	}

	/**
	 * Load TextMate grammars declared from th extension point.
	 */
	private void loadGrammars(IConfigurationElement[] cf, GrammarRegistry cache) {
		for (IConfigurationElement ce : cf) {
			String name = ce.getName();
			if ("grammar".equals(name)) {
				cache.register(new GrammarDefinition(ce));
			} else if ("scopeNameContentTypeBinding".equals(name)) {
				String contentTypeId = ce.getAttribute("contentTypeId");
				String scopeName = ce.getAttribute("scopeName");
				scopeNameBindings.put(contentTypeId, scopeName);
			}
		}
	}
}
