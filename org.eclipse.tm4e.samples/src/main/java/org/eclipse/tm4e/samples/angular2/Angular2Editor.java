package org.eclipse.tm4e.samples.angular2;

import org.eclipse.ui.editors.text.TextEditor;

public class Angular2Editor extends TextEditor {

	public Angular2Editor() {
		setSourceViewerConfiguration(new Angular2ViewerConfiguration());
	}
}
