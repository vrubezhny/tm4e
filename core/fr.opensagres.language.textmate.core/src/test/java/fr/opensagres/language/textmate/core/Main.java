package fr.opensagres.language.textmate.core;


import fr.opensagres.language.textmate.core.grammar.IGrammar;
import fr.opensagres.language.textmate.core.grammar.IToken;
import fr.opensagres.language.textmate.core.grammar.ITokenizeLineResult;
import fr.opensagres.language.textmate.core.registry.Registry;

public class Main {

	public static void main(String[] args) throws Exception {
		Registry registry = new Registry();
		IGrammar grammar = registry.loadGrammarFromPathSync("JavaScript.tmLanguage",
				Main.class.getResourceAsStream("JavaScript.tmLanguage"));
		ITokenizeLineResult lineTokens = grammar.tokenizeLine("function ");
		for (int i = 0; i < lineTokens.getTokens().length; i++) {
			IToken token = lineTokens.getTokens()[i];
			System.out.println("Token from " + token.getStartIndex() + " to " + token.getEndIndex() + " with scopes "
					+ token.getScopes());
		}
	}
}
