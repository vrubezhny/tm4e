package fr.opensagres.language.textmate.eclipse.samples.typescript;

import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import fr.opensagres.language.textmate.core.grammar.IGrammar;
import fr.opensagres.language.textmate.core.registry.Registry;
import fr.opensagres.language.textmate.eclipse.text.TMPresentationReconciler;
import fr.opensagres.language.textmate.eclipse.text.styles.CSSTokenProvider;
import fr.opensagres.language.textmate.eclipse.text.styles.ITokenProvider;

public class TypeScriptViewerConfiguration extends SourceViewerConfiguration {

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer viewer) {
		// Defines a TextMate Presentation reconcilier
		TMPresentationReconciler reconciler = new TMPresentationReconciler();
		// Set the TypeScript grammar
		reconciler.setGrammar(getGrammar());
		// Set the token provider used to style editor tokens
		reconciler.setTokenProvider(getTokenProvider());
		return reconciler;
	}

	private IGrammar getGrammar() {
		// TODO: cache the grammar
		Registry registry = new Registry();
		try {
			return registry.loadGrammarFromPathSync("TypeScript.tmLanguage",
					TypeScriptViewerConfiguration.class.getResourceAsStream("TypeScript.tmLanguage"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private ITokenProvider getTokenProvider() {
		// TODO: cache the token provider
		return new CSSTokenProvider(TypeScriptViewerConfiguration.class.getResourceAsStream("style.css"));
	}
}
