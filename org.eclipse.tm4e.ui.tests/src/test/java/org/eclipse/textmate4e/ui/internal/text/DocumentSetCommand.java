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
