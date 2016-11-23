package org.eclipse.textmate4e.core;

import org.eclipse.textmate4e.core.grammar.GrammarTest;
import org.eclipse.textmate4e.core.grammar.IGrammar;
import org.eclipse.textmate4e.core.grammar.ITokenizeLineResult;
import org.eclipse.textmate4e.core.registry.Registry;

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
