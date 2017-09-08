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
package org.eclipse.tm4e.languageconfiguration.internal.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.tm4e.languageconfiguration.internal.LanguageConfigurationMessages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * A language configuration preference page allows configuration of the language
 * configuration It provides controls for adding, removing and changing language
 * configuration as well as enablement, default management.
 */
public class LanguageConfigurationPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public final static String PAGE_ID = "org.eclipse.tm4e.languageconfiguration.LanguageConfigurationPreferencePage";

	public LanguageConfigurationPreferencePage() {
		super();
		setDescription(LanguageConfigurationMessages.LanguageConfigurationPreferencePage_description);
	}

	@Override
	protected Control createContents(Composite ancestor) {
		Composite parent = new Composite(ancestor, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		parent.setLayout(layout);

		Composite innerParent = new Composite(parent, SWT.NONE);
		GridLayout innerLayout = new GridLayout();
		innerLayout.numColumns = 2;
		innerLayout.marginHeight = 0;
		innerLayout.marginWidth = 0;
		innerParent.setLayout(innerLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		innerParent.setLayoutData(gd);

		Dialog.applyDialogFont(parent);
		innerParent.layout();

		return parent;

	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			setTitle(LanguageConfigurationMessages.LanguageConfigurationPreferencePage_title);
	}

	@Override
	public void init(IWorkbench workbench) {

	}
}
