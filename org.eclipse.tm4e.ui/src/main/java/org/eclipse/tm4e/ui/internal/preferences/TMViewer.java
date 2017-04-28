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
package org.eclipse.tm4e.ui.internal.preferences;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.ui.text.TMPresentationReconciler;

/**
 * Simple TextMate Viewer.
 *
 */
public class TMViewer extends SourceViewer {

	private TMPresentationReconciler reconciler;

	public TMViewer(Composite parent, IVerticalRuler ruler, int styles) {
		super(parent, ruler, styles);
		init();
	}

	public TMViewer(Composite parent, IVerticalRuler verticalRuler, IOverviewRuler overviewRuler,
			boolean showAnnotationsOverview, int styles) {
		super(parent, verticalRuler, overviewRuler, showAnnotationsOverview, styles);
		init();
	}

	private void init() {
		this.reconciler = new TMPresentationReconciler();
		SourceViewerConfiguration configuration = new TMSourceViewerConfiguration();
		this.configure(configuration);
	}

	private class TMSourceViewerConfiguration extends SourceViewerConfiguration {

		@Override
		public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
			return reconciler;
		}

	}

	public void setGrammar(IGrammar grammar) {
		reconciler.setGrammar(grammar);
		if (getDocument() == null) {
			super.setDocument(new Document());
		}
	}

	public void setThemeId(String themeId, String eclipseThemeId) {
		reconciler.setThemeId(themeId);
		// Hard code background color according the E4 Theme.
		// TODO: use ITheme E4 Theme to manage that.
		if ("org.eclipse.e4.ui.css.theme.e4_dark".equals(eclipseThemeId)) {
			Display display = Display.getCurrent();
			Color black = display.getSystemColor(SWT.COLOR_BLACK);
			getTextWidget().setBackground(black);
			Color gray = display.getSystemColor(SWT.COLOR_GRAY);
			getTextWidget().setForeground(gray);
		} else {
			getTextWidget().setBackground(null);
			getTextWidget().setForeground(null);
		}
	}

}
