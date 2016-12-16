package org.eclipse.tm4e.samples.html;

import org.eclipse.ui.editors.text.TextEditor;

public class HTMLEditor extends TextEditor {

	public HTMLEditor() {
		setSourceViewerConfiguration(new HTMLViewerConfiguration());
	}
}
