package fr.opensagres.language.textmate.core.internal.grammar.parser.xml;

import org.junit.Test;

import fr.opensagres.language.textmate.core.internal.grammar.reader.IGrammarParser;
import fr.opensagres.language.textmate.core.internal.types.IRawGrammar;

public class XMLPlistParserTest {

	@Test
	public void testCsharp() throws Exception {
		IGrammarParser parser = XMLPListParser.INSTANCE;
		IRawGrammar grammar = parser.parse(XMLPlistParserTest.class.getResourceAsStream("JavaScript.tmLanguage"));
		System.err.println(grammar);
	}
}
