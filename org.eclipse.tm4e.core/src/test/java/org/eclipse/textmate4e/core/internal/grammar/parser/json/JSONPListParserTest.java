package org.eclipse.tm4e.core.internal.grammar.parser.json;

import org.eclipse.tm4e.core.internal.grammar.parser.json.JSONPListParser;
import org.eclipse.tm4e.core.internal.grammar.reader.IGrammarParser;
import org.eclipse.tm4e.core.internal.types.IRawGrammar;
import org.junit.Test;

public class JSONPListParserTest {

	@Test
	public void testCsharp() throws Exception {
		IGrammarParser parser = JSONPListParser.INSTANCE;
		IRawGrammar grammar = parser.parse(JSONPListParserTest.class.getResourceAsStream("csharp.json"));
		System.err.println(grammar);
	}
}
