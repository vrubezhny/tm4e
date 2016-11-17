package org.eclipse.language.textmate.eclipse.samples.typescript;

import org.eclipse.ui.editors.text.TextEditor;

public class JSXEditor extends TextEditor {

	public JSXEditor() {
		setSourceViewerConfiguration(new JSXViewerConfiguration());
	}
}
