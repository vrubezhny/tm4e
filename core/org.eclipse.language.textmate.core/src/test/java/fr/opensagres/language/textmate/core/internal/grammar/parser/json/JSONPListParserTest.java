package fr.opensagres.language.textmate.core.internal.grammar.parser.json;

import org.junit.Test;

import fr.opensagres.language.textmate.core.internal.grammar.reader.IGrammarParser;
import fr.opensagres.language.textmate.core.internal.types.IRawGrammar;

public class JSONPListParserTest {

	@Test
	public void testCsharp() throws Exception {
		IGrammarParser parser = JSONPListParser.INSTANCE;
		IRawGrammar grammar = parser.parse(JSONPListParserTest.class.getResourceAsStream("csharp.json"));
		System.err.println(grammar);
	}
}
