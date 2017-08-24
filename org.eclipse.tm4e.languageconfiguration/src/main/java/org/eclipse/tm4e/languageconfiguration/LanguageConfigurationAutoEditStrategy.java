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
package org.eclipse.tm4e.languageconfiguration;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.tm4e.languageconfiguration.internal.LanguageConfigurationRegistry;
import org.eclipse.tm4e.languageconfiguration.internal.supports.AutoClosingPair;
import org.eclipse.tm4e.languageconfiguration.internal.supports.EnterAction;
import org.eclipse.tm4e.languageconfiguration.internal.supports.EnterActionAndIndent;
import org.eclipse.tm4e.languageconfiguration.internal.utils.TabSpacesInfo;
import org.eclipse.tm4e.languageconfiguration.internal.utils.TextUtils;
import org.eclipse.tm4e.ui.utils.ContentTypeHelper;
import org.eclipse.tm4e.ui.utils.ContentTypeInfo;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * {@link IAutoEditStrategy} which uses VSCode language-configuration.json.
 *
 */
public class LanguageConfigurationAutoEditStrategy implements IAutoEditStrategy {

	private IDocument document;
	private IContentType[] contentTypes;

	private TabSpacesInfo tabSpacesInfo;
	private ITextViewer viewer;

	@Override
	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		IContentType[] contentTypes = findContentTypes(document);
		if (contentTypes == null) {
			return;
		}
		installViewer();

		if (TextUtils.isEnter(document, command)) {
			// key enter pressed
			onEnter(document, command, false);
			return;
		}

		// Auto close pair
		LanguageConfigurationRegistry registry = LanguageConfigurationRegistry.getInstance();
		for (IContentType contentType : contentTypes) {
			List<AutoClosingPair> autoClosingPairs = registry.getAutoClosingPairs(contentType.getId());
			if (autoClosingPairs != null && autoClosingPairs.size() > 0) {
				for (AutoClosingPair autoClosingPair : autoClosingPairs) {
					if (command.text.equals(autoClosingPair.getOpen())) {
						command.text += autoClosingPair.getClose();
						command.caretOffset = command.offset + 1;
						command.shiftsCaret = false;
						break;
					}
				}
			}
		}

		// ITMModel model = TMUIPlugin.getTMModelManager().connect(document);
		// try {
		// int lineNumber = document.getLineOfOffset(command.offset);
		// model.forceTokenization(lineNumber);
		// List<TMToken> tokens = model.getLineTokens(lineNumber);
		// System.err.println(tokens.size());
		// } catch (BadLocationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// if (registry.shouldAutoClosePair(command.text, ".java")) {
		// List<IAutoClosingPair> autoClosingPairs =
		// registry.getAutoClosingPairs(".java");
		// for (IAutoClosingPair autoClosingPair : autoClosingPairs) {
		// if (command.text.equals(autoClosingPair.getOpen())) {
		// command.text += autoClosingPair.getClose();
		// command.caretOffset = command.offset + 1;
		// command.shiftsCaret = false;
		// break;
		// }
		// }
		// }
	}

	private void onEnter(IDocument document, DocumentCommand command, boolean keepPosition) {
		LanguageConfigurationRegistry registry = LanguageConfigurationRegistry.getInstance();
		for (IContentType contentType : contentTypes) {
			EnterActionAndIndent r = registry.getEnterAction(document, command.offset, contentType.getId());
			if (r != null) {
				EnterAction enterAction = r.getEnterAction();
				String indentation = r.getIndentation();
				String delim = command.text;
				switch (enterAction.getIndentAction()) {
				case None: {
					// Nothing special
					String increasedIndent = normalizeIndentation(indentation + enterAction.getAppendText());
					String typeText = delim + increasedIndent;

					command.text = typeText;
					command.shiftsCaret = false;

					if (keepPosition) {

					} else {
						command.caretOffset = command.offset + (delim + increasedIndent).length();
					}
					break;
				}
				case Indent: {
					// Indent once
					String increasedIndent = normalizeIndentation(indentation + enterAction.getAppendText());
					String typeText = delim + increasedIndent;

					command.text = typeText;
					command.shiftsCaret = false;

					if (keepPosition) {

					} else {
						command.caretOffset = command.offset + (delim + increasedIndent).length();
					}
					break;
				}
				case IndentOutdent: {
					// Ultra special
					String normalIndent = normalizeIndentation(indentation);
					String increasedIndent = normalizeIndentation(indentation + enterAction.getAppendText());

					String typeText = delim + increasedIndent + delim + normalIndent;
					command.text = typeText;
					command.shiftsCaret = false;

					if (keepPosition) {

					} else {
						command.caretOffset = command.offset + (delim + increasedIndent).length();
					}
					break;
				}
				case Outdent:
					break;
				}
				return;
			}
		}

		// no enter rules applied, we should check indentation rules then.
		String indentation = TextUtils.getIndentationAtPosition(document, command.offset);
		String increasedIndent = normalizeIndentation(indentation);
		String delim = TextUtilities.getDefaultLineDelimiter(document);
		String typeText = delim + increasedIndent;

		command.text = typeText;
		command.shiftsCaret = false;

		if (keepPosition) {

		} else {
			command.caretOffset = command.offset + (delim + increasedIndent).length();
		}
	}

	private IContentType[] findContentTypes(IDocument document) {
		if (this.document != null && this.document.equals(document)) {
			return contentTypes;
		}
		try {
			ContentTypeInfo info = ContentTypeHelper.findContentTypes(document);
			this.contentTypes = info.getContentTypes();
			this.document = document;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return contentTypes;
	}

	private String normalizeIndentation(String str) {
		TabSpacesInfo tabSpaces = getTabSpaces();
		return TextUtils.normalizeIndentation(str, tabSpaces.getTabSize(), tabSpaces.isInsertSpaces());
	}

	private TabSpacesInfo getTabSpaces() {
		// For performance reason, tab spaces info are cached.
		// If user change preferences (tab size, insert spaces), he must close the editor
		// FIXME : how to detect changes of (tab size, insert spaces) with a generic mean?
		if (tabSpacesInfo != null) {
			return tabSpacesInfo;
		}
		tabSpacesInfo = TextUtils.getTabSpaces(viewer);
		return tabSpacesInfo;
	}

	private void installViewer() {
		if (viewer == null) {
			IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			IEditorPart editorPart = page.getActiveEditor();
			ITextOperationTarget target = (ITextOperationTarget) editorPart.getAdapter(ITextOperationTarget.class);
			if (target instanceof ITextViewer) {
				viewer = (ITextViewer) target;
			}
		}
	}

}
