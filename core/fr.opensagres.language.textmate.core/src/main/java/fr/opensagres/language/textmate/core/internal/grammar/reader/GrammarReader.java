package fr.opensagres.language.textmate.core.internal.grammar.reader;

import java.io.InputStream;

import fr.opensagres.language.textmate.core.internal.grammar.parser.json.JSONPListParser;
import fr.opensagres.language.textmate.core.internal.grammar.parser.xml.XMLPListParser;
import fr.opensagres.language.textmate.core.internal.types.IRawGrammar;

public class GrammarReader {

	public static IRawGrammar readGrammarSync(String filePath, InputStream in) throws Exception {
		SyncGrammarReader reader = new SyncGrammarReader(in, getGrammarParser(filePath));
		return reader.load();
	}

	private static IGrammarParser getGrammarParser(String filePath) {
		if (filePath.endsWith(".json")) {
			return JSONPListParser.INSTANCE;
		}
		return XMLPListParser.INSTANCE;
	}
}
