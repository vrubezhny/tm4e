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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.tm4e.languageconfiguration.internal.supports.AutoClosingPair;
import org.eclipse.tm4e.languageconfiguration.internal.supports.CharacterPairSupport;
import org.eclipse.tm4e.languageconfiguration.internal.supports.EnterAction;
import org.eclipse.tm4e.languageconfiguration.internal.supports.EnterAction.IndentAction;
import org.eclipse.tm4e.languageconfiguration.internal.supports.EnterActionAndIndent;
import org.eclipse.tm4e.languageconfiguration.internal.supports.OnEnterSupport;
import org.eclipse.tm4e.languageconfiguration.internal.supports.RichEditSupport;
import org.eclipse.tm4e.languageconfiguration.internal.utils.TextUtils;

/**
 * {@link LanguageConfiguration} registry.
 *
 */
public class LanguageConfigurationRegistry {

	private static final String EXTENSION_LANGUAGE_CONFIGURATIONS = "languageConfigurations"; //$NON-NLS-1$
	private static final String LANGUAGE_CONFIGURATION_ELT = "languageConfiguration"; //$NON-NLS-1$

	private static LanguageConfigurationRegistry INSTANCE;

	public static LanguageConfigurationRegistry getInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		INSTANCE = createInstance();
		return INSTANCE;
	}

	private static synchronized LanguageConfigurationRegistry createInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		LanguageConfigurationRegistry manager = new LanguageConfigurationRegistry();
		manager.load();
		return manager;
	}

	private void load() {
		loadFromExtensionPoints();
	}

	private void loadFromExtensionPoints() {
		IConfigurationElement[] cf = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(LanguageConfigurationPlugin.PLUGIN_ID, EXTENSION_LANGUAGE_CONFIGURATIONS);
		for (IConfigurationElement ce : cf) {
			String name = ce.getName();
			if (LANGUAGE_CONFIGURATION_ELT.equals(name)) {
				LanguageConfigurationDefinition delegate = new LanguageConfigurationDefinition(ce);
				register(delegate, delegate.getContentTypeId());
			}
		}

	}

	private final Map<String, RichEditSupport> supports;

	public LanguageConfigurationRegistry() {
		this.supports = new HashMap<>();
	}

	private CharacterPairSupport _getCharacterPairSupport(String contentTypeId) {
		RichEditSupport value = this.getRichEditSupport(contentTypeId);
		if (value == null) {
			return null;
		}
		return value.getCharacterPair();
	}

	public void register(LanguageConfigurationDefinition configuration, String contentTypeId) {
		RichEditSupport current = new RichEditSupport(contentTypeId, null, configuration);
		this.supports.put(contentTypeId, current);
	}

	private RichEditSupport getRichEditSupport(String contentTypeId) {
		return supports.get(contentTypeId);
	}

	public List<AutoClosingPair> getAutoClosingPairs(String contentTypeId) {
		CharacterPairSupport characterPairSupport = this._getCharacterPairSupport(contentTypeId);
		if (characterPairSupport == null) {
			return Collections.emptyList();
		}
		return characterPairSupport.getAutoClosingPairs();
	}

	public boolean shouldAutoClosePair(String character,
			String contentTypeId/* , context: LineTokens, column: number */) {
		// let scopedLineTokens = createScopedLineTokens(context, column - 1);
		CharacterPairSupport characterPairSupport = this
				._getCharacterPairSupport(contentTypeId /* scopedLineTokens.languageId */);
		if (characterPairSupport == null) {
			return false;
		}
		return characterPairSupport
				.shouldAutoClosePair(character/* , scopedLineTokens, column - scopedLineTokens.firstCharOffset */);
	}

	public EnterActionAndIndent getEnterAction(IDocument document, int offset, String contentTypeId) {
		String indentation = TextUtils.getIndentationAtPosition(document, offset);
		// let scopedLineTokens = this.getScopedLineTokens(model, range.startLineNumber,
		// range.startColumn);
		OnEnterSupport onEnterSupport = this._getOnEnterSupport(contentTypeId /* scopedLineTokens.languageId */);
		if (onEnterSupport == null) {
			return null;
		}

		try {
			IRegion lineInfo = document.getLineInformationOfOffset(offset);

			// String scopeLineText = DocumentHelper.getLineTextOfOffset(document, offset,
			// false);
			String beforeEnterText = document.get(lineInfo.getOffset(), offset - lineInfo.getOffset());
			String afterEnterText = null;

			// selection support
			// if (range.isEmpty()) {
			afterEnterText = document.get(offset, lineInfo.getLength() - (offset - lineInfo.getOffset())); // scopedLineText.substr(range.startColumn
																											// - 1 -
																											// scopedLineTokens.firstCharOffset);
			// } else {
			// const endScopedLineTokens = this.getScopedLineTokens(model,
			// range.endLineNumber, range.endColumn);
			// afterEnterText = endScopedLineTokens.getLineContent().substr(range.endColumn
			// - 1 - scopedLineTokens.firstCharOffset);
			// }

			String oneLineAboveText = "";
			/*
			 * let lineNumber = range.startLineNumber; let oneLineAboveText = '';
			 * 
			 * if (lineNumber > 1 && scopedLineTokens.firstCharOffset === 0) { // This is
			 * not the first line and the entire line belongs to this mode let
			 * oneLineAboveScopedLineTokens = this.getScopedLineTokens(model, lineNumber -
			 * 1); if (oneLineAboveScopedLineTokens.languageId ===
			 * scopedLineTokens.languageId) { // The line above ends with text belonging to
			 * the same mode oneLineAboveText =
			 * oneLineAboveScopedLineTokens.getLineContent(); } }
			 */

			EnterAction enterResult = null;
			try {
				enterResult = onEnterSupport.onEnter(oneLineAboveText, beforeEnterText, afterEnterText);
			} catch (Exception e) {
				// onUnexpectedError(e);
			}

			if (enterResult == null) {
				return null;
			} else {
				// Here we add `\t` to appendText first because enterAction is leveraging
				// appendText and removeText to change indentation.
				if (enterResult.getAppendText() == null) {
					if ((enterResult.getIndentAction() == IndentAction.Indent)
							|| (enterResult.getIndentAction() == IndentAction.IndentOutdent)) {
						enterResult.setAppendText("\t");
					} else {
						enterResult.setAppendText("");
					}
				}
			}

			if (enterResult.getRemoveText() != null) {
				indentation = indentation.substring(0, indentation.length() - enterResult.getRemoveText());
			}

			return new EnterActionAndIndent(enterResult, indentation);

		} catch (BadLocationException e1) {
		}
		return null;
	}

	private OnEnterSupport _getOnEnterSupport(String contentTypeId) {
		RichEditSupport value = this.getRichEditSupport(contentTypeId);
		if (value == null) {
			return null;
		}
		return value.getOnEnter();

	}

}
