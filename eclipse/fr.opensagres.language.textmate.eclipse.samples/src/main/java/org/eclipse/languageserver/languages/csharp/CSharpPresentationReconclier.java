package org.eclipse.languageserver.languages.csharp;

import fr.opensagres.language.textmate.core.grammar.IGrammar;
import fr.opensagres.language.textmate.core.registry.Registry;
import fr.opensagres.language.textmate.eclipse.text.TMPresentationReconciler;
import fr.opensagres.language.textmate.eclipse.text.styles.CSSTokenProvider;
import fr.opensagres.language.textmate.eclipse.text.styles.ITokenProvider;

public class CSharpPresentationReconclier extends TMPresentationReconciler {

	public CSharpPresentationReconclier() {
		// Set the token provider used to style editor tokens
		super.setTokenProvider(initTokenProvider());
	}

	private ITokenProvider initTokenProvider() {
		// TODO: cache the token provider
		return new CSSTokenProvider(CSharpPresentationReconclier.class.getResourceAsStream("style.css"));
	}
}
