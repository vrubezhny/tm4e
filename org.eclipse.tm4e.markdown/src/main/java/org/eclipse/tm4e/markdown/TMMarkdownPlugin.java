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
package org.eclipse.tm4e.markdown;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * OSGi Activator for TextMate Markdown bundle.
 *
 */
public class TMMarkdownPlugin implements BundleActivator {

	public static final String PLUGIN_ID = "org.eclipse.tm4e.markdown";

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		TMMarkdownPlugin.context = bundleContext;
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		TMMarkdownPlugin.context = null;
	}
}