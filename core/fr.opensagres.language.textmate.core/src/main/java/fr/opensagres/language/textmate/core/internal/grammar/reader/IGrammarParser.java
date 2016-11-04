package fr.opensagres.language.textmate.core.internal.grammar.reader;

import java.io.InputStream;

import fr.opensagres.language.textmate.core.internal.types.IRawGrammar;

public interface IGrammarParser {

	IRawGrammar parse(InputStream contents) throws Exception;
}
