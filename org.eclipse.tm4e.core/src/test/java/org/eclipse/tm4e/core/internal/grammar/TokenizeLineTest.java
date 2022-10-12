package org.eclipse.tm4e.core.internal.grammar;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.tm4e.core.Data;
import org.eclipse.tm4e.core.registry.IGrammarSource;
import org.eclipse.tm4e.core.registry.Registry;
import org.junit.jupiter.api.Test;

public class TokenizeLineTest {

	@Test
	void testTokenizeLine2() throws Exception {
		final var grammar = new Registry().addGrammar(IGrammarSource.fromResource(Data.class, "JavaScript.tmLanguage"));

		final var lineText = new String("function add(a,b) { return a+b; }");
		System.out.println(lineText);
		
		final var lineTokens = grammar.tokenizeLine(lineText);
		for (int i = 0; i < lineTokens.getTokens().length; i++) {
			final var token = lineTokens.getTokens()[i];
			final var s = "Token from " + token.getStartIndex() + " to " + token.getEndIndex() + " with scopes "
				+ token.getScopes();
			System.out.println(s);
		}

		System.out.println("----------");

		final var lineTokens2 = grammar.tokenizeLine2(lineText);
		for (int i = 0; i < lineTokens2.getTokens().length; i++) {
			final int token = lineTokens2.getTokens()[i];
			System.out.println(token);
		}

		System.out.println("----------");
	}

	@Test
	void testTokenizeMultiByteLine2() throws Exception {
		final var grammar = new Registry().addGrammar(IGrammarSource.fromResource(Data.class, "c.tmLanguage.json"));

		final var lineText = new String("char cat[] = {\"кошка\"}; char mouse = -1;\n");
		System.out.println(lineText);
		
		final var lineTokens = grammar.tokenizeLine("char cat[] = {\"кошка\"}; char mouse = -1;\n");
		
		for (int i = 0; i < lineTokens.getTokens().length; i++) {
			final var token = lineTokens.getTokens()[i];
			final var s = "Token from " + token.getStartIndex() + " to " + token.getEndIndex() + " with scopes "
				+ token.getScopes();
			assertTrue(token.getStartIndex() >=0 && token.getStartIndex() <= lineText.length() &&
				token.getEndIndex() >=0 && token.getEndIndex() <= lineText.length());
			System.out.println(s);
		}

		System.out.println("----------");

		final var lineTokens2 = grammar.tokenizeLine2(lineText);
		for (int i = 0; i < lineTokens2.getTokens().length; i++) {
			final int token = lineTokens2.getTokens()[i];
			System.out.println(token);
		}

		System.out.println("----------");
	}
}
