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
package org.eclipse.textmate4e.core;

import org.eclipse.textmate4e.core.grammars.IGrammarRegistryManager;
import org.eclipse.textmate4e.core.internal.grammars.GrammarRegistryManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * OSGi Activator for TextMate Core bundle.
 *
 */
public class TMCorePlugin implements BundleActivator {

	public static final String PLUGIN_ID = "org.eclipse.textmate4e.core";

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		TMCorePlugin.context = bundleContext;
		GrammarRegistryManager.getInstance().initialize();
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		GrammarRegistryManager.getInstance().destroy();
		TMCorePlugin.context = null;
	}

	/**
	 * Returns the TextMate grammar manager.
	 * 
	 * @return the TextMate grammar manager.
	 */
	public static IGrammarRegistryManager getGrammarRegistryManager() {
		return GrammarRegistryManager.getInstance();
	}
}