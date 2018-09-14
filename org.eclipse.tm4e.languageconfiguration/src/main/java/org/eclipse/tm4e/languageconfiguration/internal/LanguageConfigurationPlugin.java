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
package org.eclipse.tm4e.languageconfiguration.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * OSGi Activator for Language Configuration (VSCode
 * language-configuration.json) Eclipse bundle.
 *
 */
public class LanguageConfigurationPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.eclipse.tm4e.languageconfiguration"; //$NON-NLS-1$

	private static LanguageConfigurationPlugin INSTANCE = null;

	public static LanguageConfigurationPlugin getInstance() {
		return INSTANCE;
	}

	@Override public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}

	@Override public void stop(BundleContext context) throws Exception {
		INSTANCE = null;
		super.stop(context);
	}
}