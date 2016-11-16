package org.eclipse.languageserver.languages.json;

import fr.opensagres.language.textmate.eclipse.text.TMPresentationReconciler;
import fr.opensagres.language.textmate.eclipse.text.styles.CSSTokenProvider;
import fr.opensagres.language.textmate.eclipse.text.styles.ITokenProvider;

public class JSONPresentationReconcilier extends TMPresentationReconciler {

	public JSONPresentationReconcilier() {
		// Set the token provider used to style editor tokens
		super.setTokenProvider(initTokenProvider());
	}

	private ITokenProvider initTokenProvider() {
		// TODO: cache the token provider
		return new CSSTokenProvider(JSONPresentationReconcilier.class.getResourceAsStream("style.css"));
	}

}
