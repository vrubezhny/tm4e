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
package org.eclipse.language.textmate.eclipse;

import org.eclipse.language.textmate.eclipse.grammars.IGrammarRegistryManager;
import org.eclipse.language.textmate.eclipse.internal.grammars.GrammarRegistryManager;
import org.eclipse.language.textmate.eclipse.internal.model.TMModelManager;
import org.eclipse.language.textmate.eclipse.internal.themes.ThemeManager;
import org.eclipse.language.textmate.eclipse.model.ITMModelManager;
import org.eclipse.language.textmate.eclipse.themes.IThemeManager;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TMPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.language.textmate.eclipse"; //$NON-NLS-1$

	// The shared instance
	private static TMPlugin plugin;

	/**
	 * The constructor
	 */
	public TMPlugin() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		ThemeManager.getInstance().initialize();
		GrammarRegistryManager.getInstance().initialize();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		ThemeManager.getInstance().destroy();
		GrammarRegistryManager.getInstance().destroy();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static TMPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the TextMate model manager.
	 * 
	 * @return the TextMate model manager.
	 */
	public static ITMModelManager getTMModelManager() {
		return TMModelManager.getInstance();
	}

	/**
	 * Returns the TextMate themes manager.
	 * 
	 * @return the TextMate themes manager.
	 */
	public static IThemeManager getThemeManager() {
		return ThemeManager.getInstance();
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
