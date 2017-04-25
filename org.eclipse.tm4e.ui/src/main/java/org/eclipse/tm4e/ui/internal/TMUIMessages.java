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
package org.eclipse.tm4e.ui.internal;

import org.eclipse.osgi.util.NLS;

/**
 * Helper class to get NLSed messages.
 *
 */
public class TMUIMessages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.tm4e.ui.internal.TMUIMessages"; //$NON-NLS-1$

	// Buttons
	public static String Button_new;
	public static String Button_remove;

	// TextMate preferences page
	public static String TextMatePreferencePage_GrammarRelatedLink;
	public static String TextMatePreferencePage_ThemeRelatedLink;
	
	// Grammar preferences page
	public static String GrammarPreferencePage_title;
	public static String GrammarPreferencePage_description;
	public static String GrammarPreferencePage_column_scopeName;
	public static String GrammarPreferencePage_column_path;
	public static String GrammarPreferencePage_column_pluginId;

	// Theme preferences page
	public static String ThemePreferencePage_title;
	public static String ThemePreferencePage_description;
	public static String ThemePreferencePage_column_name;
	public static String ThemePreferencePage_column_path;
	public static String ThemePreferencePage_column_pluginId;

	static {
		NLS.initializeMessages(BUNDLE_NAME, TMUIMessages.class);
	}

}
