package org.eclipse.tm4e.samples.php;

import org.eclipse.ui.editors.text.TextEditor;

public class PHPEditor extends TextEditor {

	public PHPEditor() {
		setSourceViewerConfiguration(new PHPViewerConfiguration());
	}
}
