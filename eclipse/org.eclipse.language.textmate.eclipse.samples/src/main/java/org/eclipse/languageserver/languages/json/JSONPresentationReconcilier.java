package org.eclipse.languageserver.languages.json;

import org.eclipse.language.textmate.eclipse.text.TMPresentationReconciler;
import org.eclipse.language.textmate.eclipse.text.styles.CSSTokenProvider;
import org.eclipse.language.textmate.eclipse.text.styles.ITokenProvider;

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
