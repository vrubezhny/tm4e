package org.eclipse.textmate4e.samples.freemarker;

import org.eclipse.ui.editors.text.TextEditor;

public class FreemarkerEditor extends TextEditor {

	public FreemarkerEditor() {
		setSourceViewerConfiguration(new FreemarkerViewerConfiguration());
	}
}
