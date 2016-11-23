package org.eclipse.textmate4e.core.internal.grammar.parser.json;

import org.eclipse.textmate4e.core.internal.grammar.parser.json.JSONPListParser;
import org.eclipse.textmate4e.core.internal.grammar.reader.IGrammarParser;
import org.eclipse.textmate4e.core.internal.types.IRawGrammar;
import org.junit.Test;

public class JSONPListParserTest {

	@Test
	public void testCsharp() throws Exception {
		IGrammarParser parser = JSONPListParser.INSTANCE;
		IRawGrammar grammar = parser.parse(JSONPListParserTest.class.getResourceAsStream("csharp.json"));
		System.err.println(grammar);
	}
}
