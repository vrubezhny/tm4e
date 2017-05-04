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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.BidiUtils;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.registry.IGrammarDefinition;
import org.eclipse.tm4e.registry.IGrammarRegistryManager;
import org.eclipse.tm4e.registry.TMEclipseRegistryPlugin;
import org.eclipse.tm4e.registry.WorkingCopyGrammarRegistryManager;
import org.eclipse.tm4e.ui.TMUIPlugin;
import org.eclipse.tm4e.ui.internal.TMUIMessages;
import org.eclipse.tm4e.ui.internal.widgets.ColumnSelectionAdapter;
import org.eclipse.tm4e.ui.internal.widgets.ColumnViewerComparator;
import org.eclipse.tm4e.ui.internal.widgets.ContentTypesBindingWidget;
import org.eclipse.tm4e.ui.internal.widgets.GrammarDefinitionContentProvider;
import org.eclipse.tm4e.ui.internal.widgets.GrammarDefinitionLabelProvider;
import org.eclipse.tm4e.ui.internal.widgets.TMViewer;
import org.eclipse.tm4e.ui.internal.widgets.ThemeAssociationsWidget;
import org.eclipse.tm4e.ui.internal.wizards.TextMateGrammarImportWizard;
import org.eclipse.tm4e.ui.snippets.ISnippet;
import org.eclipse.tm4e.ui.snippets.ISnippetManager;
import org.eclipse.tm4e.ui.themes.IThemeAssociation;
import org.eclipse.tm4e.ui.themes.IThemeManager;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.osgi.service.prefs.BackingStoreException;

/**
 * A grammar preference page allows configuration of the TextMate grammar It
 * provides controls for adding, removing and changing grammar as well as
 * enablement, default management.
 */
public class GrammarPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public final static String PAGE_ID = "org.eclipse.tm4e.internal.ui.GrammarPreferencePage";

	// Managers
	private IGrammarRegistryManager workingCopyGrammarRegistryManager;
	private IThemeManager themeManager;
	private ISnippetManager snippetManager;

	// Grammar content
	private TableViewer grammarViewer;
	private Button grammarNewButton;
	private Button grammarRemoveButton;

	// Content type bidings content
	private ContentTypesBindingWidget contentTypesWidget;
	// Theme associations content
	private ThemeAssociationsWidget themeAssociationsWidget;

	// Preview
	private TMViewer previewViewer;

	public GrammarPreferencePage() {
		super();
		setDescription(TMUIMessages.GrammarPreferencePage_description);
		setGrammarRegistryManager(
				new WorkingCopyGrammarRegistryManager(TMEclipseRegistryPlugin.getGrammarRegistryManager()));
		setThemeManager(TMUIPlugin.getThemeManager());
		setSnippetManager(TMUIPlugin.getSnippetManager());
	}

	/**
	 * Returns the grammar registry manager.
	 * 
	 * @return the grammar registry manager.
	 */
	public IGrammarRegistryManager getGrammarRegistryManager() {
		return workingCopyGrammarRegistryManager;
	}

	/**
	 * Set the grammar registry manager.
	 * 
	 * @param grammarRegistryManager
	 */
	public void setGrammarRegistryManager(IGrammarRegistryManager grammarRegistryManager) {
		this.workingCopyGrammarRegistryManager = grammarRegistryManager;
	}

	/**
	 * Returns the theme manager.
	 * 
	 * @return the theme manager.
	 */
	public IThemeManager getThemeManager() {
		return themeManager;
	}

	/**
	 * Set the theme manager.
	 * 
	 * @param themeManager
	 */
	public void setThemeManager(IThemeManager themeManager) {
		this.themeManager = themeManager;
	}

	public ISnippetManager getSnippetManager() {
		return snippetManager;
	}

	public void setSnippetManager(ISnippetManager snippetManager) {
		this.snippetManager = snippetManager;
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

		createGrammarsContent(innerParent);
		createContentTypeBindingContent(innerParent);
		createThemeAssociationsContent(innerParent);

		previewViewer = doCreateViewer(parent);
		grammarViewer.setInput(workingCopyGrammarRegistryManager);

		updateButtons();
		Dialog.applyDialogFont(parent);
		innerParent.layout();

		return parent;
	}

	/**
	 * Create grammar list content.
	 * 
	 * @param parent
	 */
	private void createGrammarsContent(Composite parent) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
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

		ColumnViewerComparator viewerComparator = new ColumnViewerComparator();

		grammarViewer = new TableViewer(table);

		TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(TMUIMessages.GrammarPreferencePage_column_scopeName);
		int minWidth = computeMinimumColumnWidth(gc, TMUIMessages.GrammarPreferencePage_column_scopeName);
		columnLayout.setColumnData(column1, new ColumnWeightData(2, minWidth, true));
		column1.addSelectionListener(new ColumnSelectionAdapter(column1, grammarViewer, 0, viewerComparator));

		TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(TMUIMessages.GrammarPreferencePage_column_path);
		minWidth = computeMinimumColumnWidth(gc, TMUIMessages.GrammarPreferencePage_column_path);
		columnLayout.setColumnData(column2, new ColumnWeightData(2, minWidth, true));
		column2.addSelectionListener(new ColumnSelectionAdapter(column2, grammarViewer, 1, viewerComparator));

		TableColumn column3 = new TableColumn(table, SWT.NONE);
		column3.setText(TMUIMessages.GrammarPreferencePage_column_pluginId);
		minWidth = computeMinimumColumnWidth(gc, TMUIMessages.GrammarPreferencePage_column_pluginId);
		columnLayout.setColumnData(column3, new ColumnWeightData(2, minWidth, true));
		column3.addSelectionListener(new ColumnSelectionAdapter(column3, grammarViewer, 2, viewerComparator));

		gc.dispose();

		grammarViewer.setLabelProvider(new GrammarDefinitionLabelProvider());
		grammarViewer.setContentProvider(new GrammarDefinitionContentProvider());
		grammarViewer.setComparator(viewerComparator);

		grammarViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent e) {
				IStructuredSelection selection = grammarViewer.getStructuredSelection();
				if (selection.isEmpty()) {
					return;
				}
				IGrammarDefinition definition = (IGrammarDefinition) (selection).getFirstElement();
				// Update button
				grammarRemoveButton.setEnabled(!getSelectedUserGrammarDefinitions().isEmpty());
				// Select grammar
				selectGrammar(definition);
			}

			private void selectGrammar(IGrammarDefinition definition) {
				String scopeName = definition.getScopeName();

				// Load the content type binding for the given grammar
				String[] contentTypes = workingCopyGrammarRegistryManager.getContentTypesForScope(scopeName);
				contentTypesWidget.setInput(contentTypes);

				IThemeAssociation selectedAssociation = null;
				IStructuredSelection oldSelection = (IStructuredSelection) themeAssociationsWidget.getSelection();
				// Load the theme associations for the given grammar
				IThemeAssociation[] themeAssociations = themeManager.getThemeAssociationsForScope(scopeName);
				themeAssociationsWidget.setInput(themeAssociations);
				// Try to keep selection
				if (!oldSelection.isEmpty()
						&& Arrays.asList(themeAssociations).contains(oldSelection.getFirstElement())) {
					selectedAssociation = (IThemeAssociation) oldSelection.getFirstElement();
					themeAssociationsWidget.setSelection(oldSelection);
				} else {
					selectedAssociation = themeAssociations != null && themeAssociations.length > 0
							? themeAssociations[0] : null;
					if (selectedAssociation != null) {
						themeAssociationsWidget.setSelection(new StructuredSelection(selectedAssociation));
					}
				}

				// Preview the grammar
				IGrammar grammar = workingCopyGrammarRegistryManager.getGrammarForScope(scopeName);
				if (selectedAssociation != null) {
					previewViewer.setThemeId(selectedAssociation.getThemeId(), selectedAssociation.getEclipseThemeId());
				}
				previewViewer.setGrammar(grammar);
				
				// Snippet
				ISnippet[] snippets = snippetManager.getSnippets(scopeName);
				if (snippets != null && snippets.length > 0) {
					// TODO: manage list of snippet for the given scope.
					previewViewer.setText(snippets[0].getContent());
				}

			}

		});

		// Specify default sorting
		table.setSortColumn(column1);
		table.setSortDirection(viewerComparator.getDirection());

		BidiUtils.applyTextDirection(grammarViewer.getControl(), BidiUtils.BTD_DEFAULT);

		Composite buttons = new Composite(parent, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);

		grammarNewButton = new Button(buttons, SWT.PUSH);
		grammarNewButton.setText(TMUIMessages.Button_new);
		grammarNewButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grammarNewButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				add();
			}

			private void add() {
				// Open import wizard for TextMate grammar.
				TextMateGrammarImportWizard wizard = new TextMateGrammarImportWizard(false);
				wizard.setGrammarRegistryManager(workingCopyGrammarRegistryManager);
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				if (dialog.open() == Window.OK) {
					// User grammar was saved, refresh the list of grammar and
					// select the created grammar.
					IGrammarDefinition created = wizard.getCreatedDefinition();
					grammarViewer.refresh();
					grammarViewer.setSelection(new StructuredSelection(created));
				}
			}
		});

		grammarRemoveButton = new Button(buttons, SWT.PUSH);
		grammarRemoveButton.setText(TMUIMessages.Button_remove);
		grammarRemoveButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		grammarRemoveButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				remove();
			}

			private void remove() {
				Collection<IGrammarDefinition> definitions = getSelectedUserGrammarDefinitions();
				if (!definitions.isEmpty()) {
					for (IGrammarDefinition definition : definitions) {
						workingCopyGrammarRegistryManager.removeGrammarDefinition(definition);
					}
					grammarViewer.refresh();
				}
			}
		});
	}

	/**
	 * Create scopeName ContentType Binding content.
	 * 
	 * @param parent
	 */
	private void createContentTypeBindingContent(Composite parent) {
		contentTypesWidget = new ContentTypesBindingWidget(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		contentTypesWidget.setLayoutData(data);
	}

	/**
	 * Create theme associations content.
	 * 
	 * @param parent
	 */
	private void createThemeAssociationsContent(Composite parent) {
		themeAssociationsWidget = new ThemeAssociationsWidget(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		themeAssociationsWidget.setLayoutData(data);
		themeAssociationsWidget.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent e) {
				IThemeAssociation association = (IThemeAssociation) ((IStructuredSelection) e.getSelection())
						.getFirstElement();
				selectTheme(association);
			}

			private void selectTheme(IThemeAssociation association) {
				String themeId = association.getThemeId();
				String eclipseThemeId = association.getEclipseThemeId();
				previewViewer.setThemeId(themeId, eclipseThemeId);
			}
		});
	}

	private int computeMinimumColumnWidth(GC gc, String string) {
		return gc.stringExtent(string).x + 10; // pad 10 to accommodate table
												// header trimmings
	}

	private void updateButtons() {
		grammarRemoveButton.setEnabled(false);
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

	private TMViewer doCreateViewer(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(TMUIMessages.GrammarPreferencePage_preview);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		label.setLayoutData(data);

		TMViewer viewer = createViewer(parent);

		// Don't set caret to 'null' as this causes
		// https://bugs.eclipse.org/293263
		// viewer.getTextWidget().setCaret(null);

		Control control = viewer.getControl();
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		data.heightHint = convertHeightInCharsToPixels(5);
		control.setLayoutData(data);

		return viewer;
	}

	/**
	 * Creates, configures and returns a source viewer to present the template
	 * pattern on the preference page. Clients may override to provide a custom
	 * source viewer featuring e.g. syntax coloring.
	 *
	 * @param parent
	 *            the parent control
	 * @return a configured source viewer
	 */
	protected TMViewer createViewer(Composite parent) {
		return new TMViewer(parent, null, null, false, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	}

	@Override
	public boolean performOk() {
		try {
			// Save the working copy if there are some changed.
			workingCopyGrammarRegistryManager.save();
		} catch (BackingStoreException e) {
			e.printStackTrace();
			return false;
		}
		return super.performOk();
	}

	/**
	 * Returns list of selected grammar definitions which was created by the
	 * user.
	 * 
	 * @return list of selected grammar definitions which was created by the
	 *         user.
	 */
	private Collection<IGrammarDefinition> getSelectedUserGrammarDefinitions() {
		IStructuredSelection selection = grammarViewer.getStructuredSelection();
		if (selection.isEmpty()) {
			return Collections.emptyList();
		}
		return ((Collection<IGrammarDefinition>) selection.toList()).stream()
				.filter(definition -> definition.getPluginId() == null).collect(Collectors.toList());

	}
}
