package org.eclipse.textmate4e.core.internal.grammar.reader;

import java.io.InputStream;

import org.eclipse.textmate4e.core.internal.types.IRawGrammar;

public interface IGrammarParser {

	IRawGrammar parse(InputStream contents) throws Exception;
}
