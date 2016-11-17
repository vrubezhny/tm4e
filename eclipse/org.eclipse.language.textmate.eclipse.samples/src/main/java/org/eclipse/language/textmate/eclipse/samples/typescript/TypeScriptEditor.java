package org.eclipse.language.textmate.eclipse.samples.typescript;

import org.eclipse.ui.editors.text.TextEditor;

public class TypeScriptEditor extends TextEditor {

	public TypeScriptEditor() {
		setSourceViewerConfiguration(new TypeScriptViewerConfiguration());
	}
}
