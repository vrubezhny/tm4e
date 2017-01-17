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
package org.eclipse.tm4e.ui.text;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextPresentation;

/**
 * TextMate presentation reconciler listener.
 *
 */
public interface ITMPresentationReconcilerListener {

	/**
	 * Install the given viewer and document.
	 * 
	 * @param viewer
	 * @param document
	 */
	void install(ITextViewer viewer, IDocument document);

	/**
	 * Uninstall.
	 */
	void uninstall();

	/**
	 * Colorize the StyledText with the given text presentation.
	 * 
	 * @param presentation
	 *            the text presentation.
	 * @param error
	 *            when there are error.
	 */
	void colorize(TextPresentation presentation, Throwable error);
}
