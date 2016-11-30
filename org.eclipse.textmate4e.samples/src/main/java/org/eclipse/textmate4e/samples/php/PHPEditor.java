package org.eclipse.textmate4e.samples.php;

import org.eclipse.ui.editors.text.TextEditor;

public class PHPEditor extends TextEditor {

	public PHPEditor() {
		setSourceViewerConfiguration(new PHPViewerConfiguration());
	}
}
