package org.eclipse.tm4e.samples.yaml;

import org.eclipse.ui.editors.text.TextEditor;

public class YAMLEditor extends TextEditor {

	public YAMLEditor() {
		setSourceViewerConfiguration(new YAMLViewerConfiguration());
	}
}
