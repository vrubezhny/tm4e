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
package org.eclipse.textmate4e.ui.internal.text;

import org.eclipse.jface.text.Document;

public class DocumentSetCommand extends Command {

	private final String text;
	private final Document document;

	public DocumentSetCommand(String text, Document document) {
		super(getName(text));
		this.text = text;
		this.document = document;
	}

	public static String getName(String text) {
		return "document.set(\"" + toText(text) + "\");";
	}

	@Override
	protected void doExecute() {
		document.set(text);
	}

	@Override
	protected Integer getLineTo() {
		int numberOfLines =document.getNumberOfLines();
		return numberOfLines > 0 ? numberOfLines - 1 : null;
	}
}
