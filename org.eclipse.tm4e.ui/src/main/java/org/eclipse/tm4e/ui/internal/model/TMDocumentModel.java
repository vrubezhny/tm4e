package org.eclipse.tm4e.ui.internal.model;

import org.eclipse.jface.text.IDocument;
import org.eclipse.tm4e.core.model.TMModel;

public class TMDocumentModel extends TMModel {

	private IDocument document;

	public TMDocumentModel(IDocument document) {
		super(new DocumentLineList(document));
		this.document = document;
	}

	public IDocument getDocument() {
		return document;
	}

}
