package _editor;

import fr.opensagres.language.textmate.grammar.IGrammar;
import fr.opensagres.language.textmate.grammar.IToken;
import fr.opensagres.language.textmate.grammar.ITokenizeLineResult;
import fr.opensagres.language.textmate.registry.Registry;

public class Main {

	public static void main(String[] args) throws Exception {
		Registry registry = new Registry();
		IGrammar grammar = registry.loadGrammarFromPathSync("JavaScript.tmLanguage",
				Main.class.getResourceAsStream("JavaScript.tmLanguage"));
		ITokenizeLineResult lineTokens = grammar.tokenizeLine("function add(a,b) { return a+b; }");
		for (int i = 0; i < lineTokens.getTokens().length; i++) {
			IToken token = lineTokens.getTokens()[i];
			System.out.println("Token from " + token.getStartIndex() + " to " + token.getEndIndex() + " with scopes "
					+ token.getScopes());
		}
	}
}
