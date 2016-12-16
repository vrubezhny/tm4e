package org.eclipse.tm4e.core.internal.grammar.parser.xml;

import org.eclipse.tm4e.core.internal.grammar.parser.xml.XMLPListParser;
import org.eclipse.tm4e.core.internal.grammar.reader.IGrammarParser;
import org.eclipse.tm4e.core.internal.types.IRawGrammar;
import org.junit.Test;

public class XMLPlistParserTest {

	@Test
	public void testCsharp() throws Exception {
		IGrammarParser parser = XMLPListParser.INSTANCE;
		IRawGrammar grammar = parser.parse(XMLPlistParserTest.class.getResourceAsStream("JavaScript.tmLanguage"));
		System.err.println(grammar);
	}
}
