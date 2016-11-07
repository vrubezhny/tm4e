package fr.opensagres.language.textmate.eclipse.samples.csharp;

import org.eclipse.ui.editors.text.TextEditor;

public class CSharpEditor extends TextEditor {

	public CSharpEditor() {
		setSourceViewerConfiguration(new CSharpViewerConfiguration());
	}
}
