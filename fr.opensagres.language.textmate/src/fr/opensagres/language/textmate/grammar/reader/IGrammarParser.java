package fr.opensagres.language.textmate.grammar.reader;

import java.io.InputStream;

import fr.opensagres.language.textmate.types.IRawGrammar;

public interface IGrammarParser {

	IRawGrammar parse(InputStream contents) throws Exception;
}
