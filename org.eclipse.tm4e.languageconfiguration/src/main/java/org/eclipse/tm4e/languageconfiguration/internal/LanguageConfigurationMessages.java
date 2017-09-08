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

import org.eclipse.osgi.util.NLS;

/**
 * Helper class to get NLSed messages.
 *
 */
public class LanguageConfigurationMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.tm4e.languageconfiguration.internal.LanguageConfigurationMessages"; //$NON-NLS-1$

	// Language configuration preferences page
	public static String LanguageConfigurationPreferencePage_title;
	public static String LanguageConfigurationPreferencePage_description;

	static {
		NLS.initializeMessages(BUNDLE_NAME, LanguageConfigurationMessages.class);
	}

}
