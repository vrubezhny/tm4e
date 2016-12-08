package org.eclipse.textmate4e.core.grammar;

import org.eclipse.textmate4e.core.Data;
import org.eclipse.textmate4e.core.registry.Registry;

public class MarkDown {

	public static void main(String[] args) throws Exception {
		Registry registry = new Registry();
		String path = "Markdown.tmLanguage";
		IGrammar grammar = registry.loadGrammarFromPathSync(path, Data.class.getResourceAsStream(path));
		ITokenizeLineResult lineTokens = grammar.tokenizeLine("An h1 header\n============\nParagraphs are separated by a blank line.");
		for (int i = 0; i < lineTokens.getTokens().length; i++) {
			IToken token = lineTokens.getTokens()[i];
			String s = "Token from " + token.getStartIndex() + " to " + token.getEndIndex() + " with scopes "
					+ token.getScopes();
			System.err.println(s);
		}
	}
}
