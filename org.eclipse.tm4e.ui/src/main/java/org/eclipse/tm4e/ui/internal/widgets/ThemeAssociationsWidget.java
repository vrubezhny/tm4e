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
package org.eclipse.tm4e.ui.internal.widgets;

import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tm4e.registry.IGrammarDefinition;
import org.eclipse.tm4e.ui.internal.TMUIMessages;
import org.eclipse.tm4e.ui.internal.wizards.CreateThemeAssociationWizard;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.tm4e.ui.themes.IThemeManager;

/**
 * Widget which displays theme associations list on the left and "New", "Remove"
 * buttons on the right.
 *
 */
public class ThemeAssociationsWidget extends TableAndButtonsWidget {

	private IThemeManager themeManager;

	private Button newButton;
	private Button removeButton;

	private IGrammarDefinition definition;

	public ThemeAssociationsWidget(IThemeManager themeManager, Composite parent, int style) {
		super(parent, style, TMUIMessages.ThemeAssociationsWidget_description);
		this.themeManager = themeManager;
		super.setContentProvider(ArrayContentProvider.getInstance());
		super.setLabelProvider(new ThemeAssociationLabelProvider());
	}

	@Override
	protected void createButtons(Composite parent) {
		newButton = new Button(parent, SWT.PUSH);
		newButton.setText(TMUIMessages.Button_new);
		newButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		newButton.addListener(SWT.Selection, (e) -> {
			// Open the wizard to create association between theme and grammar.
			CreateThemeAssociationWizard wizard = new CreateThemeAssociationWizard(false);
			wizard.setInitialDefinition(definition);
			wizard.setThemeManager(themeManager);
			WizardDialog dialog = new WizardDialog(getShell(), wizard);
			if (dialog.open() == Window.OK) {
				IThemeAssociation association = wizard.getCreatedThemeAssociation();
				refresh(association);
			}
		});
		newButton.setEnabled(false);

		removeButton = new Button(parent, SWT.PUSH);
		removeButton.setText(TMUIMessages.Button_remove);
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		removeButton.addListener(SWT.Selection, (e) -> {

			if (MessageDialog.openConfirm(getShell(), TMUIMessages.ThemeAssociationsWidget_remove_dialog_title,
					TMUIMessages.ThemeAssociationsWidget_remove_dialog_message)) {
				IStructuredSelection selection = super.getSelection();
				Iterator<IThemeAssociation> it = selection.iterator();
				while (it.hasNext()) {
					IThemeAssociation association = it.next();
					themeManager.unregisterThemeAssociation(association);
				}
				refresh(null);
			}

		});
		removeButton.setEnabled(false);
	}

	public Button getNewButton() {
		return newButton;
	}

	public Button getRemoveButton() {
		return removeButton;
	}

	public IThemeAssociation[] setGrammarDefinition(IGrammarDefinition definition) {
		this.definition = definition;
		return refresh(null);
	}

	private IThemeAssociation[] refresh(IThemeAssociation association) {
		IThemeAssociation[] themeAssociations = themeManager.getThemeAssociationsForScope(definition.getScopeName());
		// Refresh the list of associations
		super.setInput(themeAssociations);
		// Select the first of given association
		if (association == null && themeAssociations != null && themeAssociations.length > 0) {
			association = themeAssociations[0];
		}
		if (association != null) {
			super.setSelection(new StructuredSelection(association));
		}
		return themeAssociations;
	}

}
