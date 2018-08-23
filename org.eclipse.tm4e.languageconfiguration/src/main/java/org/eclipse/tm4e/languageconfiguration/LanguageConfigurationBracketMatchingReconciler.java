/**
 *  Copyright (c) 2018 Red Hat Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Lucas Bullen (Red Hat Inc.) - initial API and implementation
 */
package org.eclipse.tm4e.languageconfiguration;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.reconciler.MonoReconciler;

@Deprecated
// Remove this class when https://git.eclipse.org/r/#/c/127756/ will be merged
public class LanguageConfigurationBracketMatchingReconciler extends MonoReconciler {
	public LanguageConfigurationBracketMatchingReconciler() {
		super(new LanguageConfigurationBracketMatchingReconcilingStrategy(), false);
	}

	@Override
	public void install(ITextViewer textViewer) {
		super.install(textViewer);
		((LanguageConfigurationBracketMatchingReconcilingStrategy) getReconcilingStrategy(
				IDocument.DEFAULT_CONTENT_TYPE)).install(textViewer);
	}

	@Override
	public void uninstall() {
		super.uninstall();
		((LanguageConfigurationBracketMatchingReconcilingStrategy) getReconcilingStrategy(
				IDocument.DEFAULT_CONTENT_TYPE)).uninstall();
	}
}