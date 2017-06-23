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
package org.eclipse.tm4e.ui.internal.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.tm4e.registry.IGrammarDefinition;
import org.eclipse.tm4e.registry.TMEclipseRegistryPlugin;
import org.eclipse.tm4e.ui.TMUIPlugin;
import org.eclipse.tm4e.ui.internal.TMUIMessages;
import org.eclipse.tm4e.ui.internal.themes.ThemeAssociation;
import org.eclipse.tm4e.ui.internal.widgets.EclipseThemeLabelProvider;
import org.eclipse.tm4e.ui.internal.widgets.GrammarDefinitionContentProvider;
import org.eclipse.tm4e.ui.internal.widgets.GrammarDefinitionLabelProvider;
import org.eclipse.tm4e.ui.internal.widgets.ThemeContentProvider;
import org.eclipse.tm4e.ui.internal.widgets.ThemeLabelProvider;
import org.eclipse.tm4e.ui.themes.ITheme;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.ui.PlatformUI;

/**
 * Wizard page to create association between grammar and theme.
 *
 */
public class CreateThemeAssociationWizardPage extends AbstractWizardPage {

	private static final String PAGE_NAME = CreateThemeAssociationWizardPage.class.getName();

	private ComboViewer themeViewer;
	private ComboViewer grammarViewer;
	private ComboViewer eclipseThemeViewer;
	private final IGrammarDefinition initialDefinition;

	protected CreateThemeAssociationWizardPage(IGrammarDefinition initialDefinition) {
		super(PAGE_NAME);
		super.setTitle(TMUIMessages.CreateThemeAssociationWizardPage_title);
		super.setDescription(TMUIMessages.CreateThemeAssociationWizardPage_description);
		this.initialDefinition = initialDefinition;
	}

	@Override
	protected void createBody(Composite ancestor) {
		Composite parent = new Composite(ancestor, SWT.NONE);
		parent.setFont(parent.getFont());
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		parent.setLayout(new GridLayout(4, false));

		// TextMate theme
		Label label = new Label(parent, SWT.NONE);
		label.setText(TMUIMessages.CreateThemeAssociationWizardPage_theme_text);
		themeViewer = new ComboViewer(parent);
		themeViewer.setLabelProvider(new ThemeLabelProvider());
		themeViewer.setContentProvider(new ThemeContentProvider());
		themeViewer.setInput(TMUIPlugin.getThemeManager());
		themeViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		themeViewer.getControl().addListener(SWT.Selection, this);

		label = new Label(parent, SWT.NONE);
		label.setText(TMUIMessages.CreateThemeAssociationWizardPage_grammar_text);
		grammarViewer = new ComboViewer(parent);
		grammarViewer.setLabelProvider(new GrammarDefinitionLabelProvider());
		grammarViewer.setContentProvider(new GrammarDefinitionContentProvider());
		grammarViewer.setInput(TMEclipseRegistryPlugin.getGrammarRegistryManager());
		grammarViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grammarViewer.getControl().addListener(SWT.Selection, this);

		if (initialDefinition != null) {
			grammarViewer.setSelection(new StructuredSelection(initialDefinition));
		}
		
		IThemeEngine themeEngine = PlatformUI.getWorkbench().getService(IThemeEngine.class);
		if (themeEngine != null) {
			label = new Label(parent, SWT.NONE);
			label.setText(TMUIMessages.CreateThemeAssociationWizardPage_e4Theme_text);
			eclipseThemeViewer = new ComboViewer(parent);
			eclipseThemeViewer.setLabelProvider(new EclipseThemeLabelProvider());
			eclipseThemeViewer.setContentProvider(ArrayContentProvider.getInstance());
			eclipseThemeViewer.setInput(themeEngine.getThemes());
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 3;
			eclipseThemeViewer.getControl().setLayoutData(data);
		}
	}

	@Override
	protected void initializeDefaultValues() {
		setPageComplete(false);
	}

	@Override
	protected IStatus validatePage(Event event) {
		if (themeViewer.getSelection().isEmpty()) {
			return new Status(IStatus.ERROR, TMUIPlugin.PLUGIN_ID,
					TMUIMessages.CreateThemeAssociationWizardPage_theme_error_required);
		}
		if (grammarViewer.getSelection().isEmpty()) {
			return new Status(IStatus.ERROR, TMUIPlugin.PLUGIN_ID,
					TMUIMessages.CreateThemeAssociationWizardPage_grammar_error_required);
		}
		return null;
	}

	public IThemeAssociation getThemeAssociation() {
		String themeId = ((ITheme) themeViewer.getStructuredSelection().getFirstElement()).getId();
		String eclipseThemeId = null;
		String scopeName = ((IGrammarDefinition) grammarViewer.getStructuredSelection().getFirstElement())
				.getScopeName();
		if (eclipseThemeViewer != null) {
			IStructuredSelection selection = eclipseThemeViewer.getStructuredSelection();
			if (!selection.isEmpty()) {
				eclipseThemeId = ((org.eclipse.e4.ui.css.swt.theme.ITheme) selection.getFirstElement()).getId();
			}
		}
		return new ThemeAssociation(themeId, eclipseThemeId, scopeName, false);
	}

}
