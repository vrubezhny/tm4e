package org.eclipse.language.textmate.core;

import org.eclipse.language.textmate.core.Main;
import org.eclipse.language.textmate.core.grammar.IGrammar;
import org.eclipse.language.textmate.core.grammar.ITokenizeLineResult;
import org.eclipse.language.textmate.core.registry.Registry;

public class TestGrammar {

	public static void main(String[] args) throws Exception {
		
		Registry registry = new Registry();
		IGrammar grammar = registry.loadGrammarFromPathSync("Angular2TypeScript.tmLanguage",
				TestGrammar.class.getResourceAsStream("Angular2TypeScript.tmLanguage"));
		
		ITokenizeLineResult result = grammar.tokenizeLine("/** **/");
		for (int i = 0; i < result.getTokens().length; i++) {
			System.err.println(result.getTokens()[i]);
		}
	}
}
