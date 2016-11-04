package fr.opensagres.language.textmate.core.internal.grammar.reader;

import java.io.InputStream;

import fr.opensagres.language.textmate.core.internal.types.IRawGrammar;

public class SyncGrammarReader {

	private InputStream in;
	private IGrammarParser _parser;

	SyncGrammarReader(InputStream in, IGrammarParser parser) {
		this.in = in;
		this._parser = parser;
	}

	public IRawGrammar load() throws Exception {
		return this._parser.parse(in);
	}
}
