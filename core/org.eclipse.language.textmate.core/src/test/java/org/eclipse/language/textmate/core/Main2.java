package org.eclipse.language.textmate.core;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.language.textmate.core.grammar.IGrammar;
import org.eclipse.language.textmate.core.grammar.IToken;
import org.eclipse.language.textmate.core.grammar.ITokenizeLineResult;
import org.eclipse.language.textmate.core.registry.Registry;

public class Main2 {

	public static void main(String[] args) throws Exception {
		Registry registry = new Registry();
		IGrammar grammar = registry.loadGrammarFromPathSync("JavaScript.tmLanguage",
				Main2.class.getResourceAsStream("JavaScript.tmLanguage"));

		String t = readFile("jquery.js");

		ITokenizeLineResult lineTokens = grammar.tokenizeLine(t);
		for (int i = 0; i < lineTokens.getTokens().length; i++) {
			IToken token = lineTokens.getTokens()[i];
			System.out.println("Token from " + token.getStartIndex() + " to " + token.getEndIndex() + " with scopes "
					+ token.getScopes());
		}
	}

	private static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		try {
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
				stringBuilder.append(ls);
			}

			return stringBuilder.toString();
		} finally {
			reader.close();
		}
	}
}
