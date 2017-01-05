package org.eclipse.tm4e.samples.typescript;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.registry.Registry;
import org.eclipse.tm4e.ui.text.TMPresentationReconciler;
import org.eclipse.tm4e.ui.themes.ITokenProvider;
import org.eclipse.tm4e.ui.themes.css.CSSTokenProvider;

public class JSXViewerConfiguration extends SourceViewerConfiguration {

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer viewer) {
		// Defines a TextMate Presentation reconcilier
		TMPresentationReconciler reconciler = new TMPresentationReconciler();
		// Set the TypeScript grammar
		reconciler.setGrammar(getGrammar());
		return reconciler;
	}

	private IGrammar getGrammar() {
		// TODO: cache the grammar
		Registry registry = new Registry();
		try {
			return registry.loadGrammarFromPathSync("TypeScriptReact.tmLanguage.json",
					JSXViewerConfiguration.class.getResourceAsStream("TypeScriptReact.tmLanguage.json"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
