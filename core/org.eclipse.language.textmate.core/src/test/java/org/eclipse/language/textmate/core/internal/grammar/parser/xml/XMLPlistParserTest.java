package org.eclipse.language.textmate.core.internal.grammar.parser.xml;

import org.eclipse.language.textmate.core.internal.grammar.reader.IGrammarParser;
import org.eclipse.language.textmate.core.internal.types.IRawGrammar;
import org.junit.Test;

public class XMLPlistParserTest {

	@Test
	public void testCsharp() throws Exception {
		IGrammarParser parser = XMLPListParser.INSTANCE;
		IRawGrammar grammar = parser.parse(XMLPlistParserTest.class.getResourceAsStream("JavaScript.tmLanguage"));
		System.err.println(grammar);
	}
}
