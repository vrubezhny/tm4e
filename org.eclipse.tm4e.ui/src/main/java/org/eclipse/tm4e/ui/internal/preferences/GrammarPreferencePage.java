/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Nicolaj Hoess <nicohoess@gmail.com> - Editor templates pref page: Allow to sort by column - https://bugs.eclipse.org/203722
 *     Angelo Zerr <angelo.zerr@gmail.com> - Adapt org.eclipse.ui.texteditor.templates.TemplatePreferencePage for TextMate grammar
 *******************************************************************************/
package org.eclipse.tm4e.ui.internal.preferences;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.BidiUtils;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tm4e.registry.IGrammarRegistryManager;
import org.eclipse.tm4e.registry.TMEclipseRegistryPlugin;
import org.eclipse.tm4e.ui.internal.TMUIMessages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * A grammar preference page allows configuration of the TextMate grammar It
 * provides controls for adding, removing and changing grammar as well as
 * enablement, default management.
 */
public class GrammarPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private TableViewer fTableViewer;
	private IGrammarRegistryManager grammarRegistryManager;
	private Button fNewButton;
	private Button fRemoveButton;

	public GrammarPreferencePage() {
		super();
		setDescription(TMUIMessages.GrammarPreferencePage_description);
		setGrammarRegistryManager(TMEclipseRegistryPlugin.getGrammarRegistryManager());
	}

	/**
	 * Returns the grammar registry manager.
	 * 
	 * @return the grammar registry manager.
	 */
	public IGrammarRegistryManager getGrammarRegistryManager() {
		return grammarRegistryManager;
	}

	/**
	 * Set the grammar registry manager.
	 * 
	 * @param grammarRegistryManager
	 */
	public void setGrammarRegistryManager(IGrammarRegistryManager grammarRegistryManager) {
		this.grammarRegistryManager = grammarRegistryManager;
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

		Composite tableComposite = new Composite(innerParent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 360;
		data.heightHint = convertHeightInCharsToPixels(10);
		tableComposite.setLayoutData(data);

		TableColumnLayout columnLayout = new TableColumnLayout();
		tableComposite.setLayout(columnLayout);
		Table table = new Table(tableComposite,
				SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		GC gc = new GC(getShell());
		gc.setFont(JFaceResources.getDialogFont());

		GrammarDefinitionViewerComparator viewerComparator = new GrammarDefinitionViewerComparator();

		fTableViewer = new TableViewer(table);

		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(TMUIMessages.GrammarPreferencePage_column_scopeName);
		int minWidth = computeMinimumColumnWidth(gc, TMUIMessages.GrammarPreferencePage_column_scopeName);
		columnLayout.setColumnData(column1, new ColumnWeightData(2, minWidth, true));
		column1.addSelectionListener(
				new GrammarDefinitionColumnSelectionAdapter(column1, fTableViewer, 0, viewerComparator));

		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(TMUIMessages.GrammarPreferencePage_column_path);
		minWidth = computeMinimumColumnWidth(gc, TMUIMessages.GrammarPreferencePage_column_path);
		columnLayout.setColumnData(column2, new ColumnWeightData(2, minWidth, true));
		column2.addSelectionListener(
				new GrammarDefinitionColumnSelectionAdapter(column2, fTableViewer, 1, viewerComparator));

		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText(TMUIMessages.GrammarPreferencePage_column_pluginId);
		minWidth = computeMinimumColumnWidth(gc, TMUIMessages.GrammarPreferencePage_column_pluginId);
		columnLayout.setColumnData(column3, new ColumnWeightData(2, minWidth, true));
		column3.addSelectionListener(
				new GrammarDefinitionColumnSelectionAdapter(column3, fTableViewer, 2, viewerComparator));

		gc.dispose();

		fTableViewer.setLabelProvider(new GrammarDefinitionLabelProvider());
		fTableViewer.setContentProvider(new GrammarDefinitionContentProvider());
		fTableViewer.setComparator(viewerComparator);

		// Specify default sorting
		table.setSortColumn(column1);
		table.setSortDirection(viewerComparator.getDirection());

		BidiUtils.applyTextDirection(fTableViewer.getControl(), BidiUtils.BTD_DEFAULT);

		Composite buttons = new Composite(innerParent, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);

		fNewButton = new Button(buttons, SWT.PUSH);
		fNewButton.setText(TMUIMessages.Button_new);
		fNewButton.setLayoutData(getButtonGridData(fNewButton));
		fNewButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				// add();
			}
		});

		fRemoveButton = new Button(buttons, SWT.PUSH);
		fRemoveButton.setText(TMUIMessages.Button_remove);
		fRemoveButton.setLayoutData(getButtonGridData(fRemoveButton));
		fRemoveButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				// remove();
			}
		});

		fTableViewer.setInput(grammarRegistryManager);

		updateButtons();
		Dialog.applyDialogFont(parent);
		innerParent.layout();

		return parent;
	}

	private int computeMinimumColumnWidth(GC gc, String string) {
		return gc.stringExtent(string).x + 10; // pad 10 to accommodate table
												// header trimmings
	}

	/**
	 * Return the grid data for the button.
	 *
	 * @param button
	 *            the button
	 * @return the grid data
	 */
	private static GridData getButtonGridData(Button button) {
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		// TODO replace SWTUtil
		// data.widthHint= SWTUtil.getButtonWidthHint(button);
		// data.heightHint= SWTUtil.getButtonHeightHint(button);

		return data;
	}

	private void updateButtons() {
		fNewButton.setEnabled(false);
		fRemoveButton.setEnabled(false);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible)
			setTitle(TMUIMessages.GrammarPreferencePage_title);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

}
