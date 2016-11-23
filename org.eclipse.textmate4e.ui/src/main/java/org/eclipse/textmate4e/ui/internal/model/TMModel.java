/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.textmate4e.ui.internal.model;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.textmate4e.core.model.AbstractTMModel;

/**
 * TextMate model implementation linked to an {@link IDocument}.
 */
public class TMModel extends AbstractTMModel {

	private final IDocument document;
	private final InternalListener listener;

	public TMModel(IDocument document) {
		this.document = document;
		this.listener = new InternalListener();
		document.addDocumentListener(listener);
	}

	private class InternalListener implements IDocumentListener {

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
			// Initialize lines if needed
			TMModel.this.initializeIfNeeded();
			try {
				if (!DocumentHelper.isInsert(event)) {
					// Remove or Replace (Remove + Insert)
					removeLine(event);
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}

		private void removeLine(DocumentEvent event) throws BadLocationException {
			int startLine = DocumentHelper.getStartLine(event);
			int endLine = DocumentHelper.getEndLine(event, true);
			for (int i = endLine; i > startLine; i--) {
				getLines().removeLine(i);
			}
		}

		@Override
		public void documentChanged(DocumentEvent event) {
			IDocument document = event.getDocument();
			try {
				int startLine = DocumentHelper.getStartLine(event);
				if (!DocumentHelper.isRemove(event)) {
					if (getLines().getSize() != DocumentHelper.getNumberOfLines(document)) {
						int endLine = DocumentHelper.getEndLine(event, false);
						// Insert new lines
						for (int i = startLine; i < endLine; i++) {
							getLines().addLine(i + 1);
						}
					} else {
						// Update line
						getLines().updateLine(startLine);
					}
				}
				_invalidateLine(startLine);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
			System.err.println(getLines().getSize() + " = " + document.getNumberOfLines());
		}
	}

	@Override
	protected int getNumberOfLines() {
		return DocumentHelper.getNumberOfLines(document);
	}

	@Override
	protected String getLineText(int line) throws Exception {
		return DocumentHelper.getLineText(document, line, false);
	}

	@Override
	protected int getLineLength(int line) throws Exception {
		return DocumentHelper.getLineLength(document, line);
	}

	@Override
	public void dispose() {
		super.dispose();
		document.removeDocumentListener(listener);
	}

	public IDocument getDocument() {
		return document;
	}

}
