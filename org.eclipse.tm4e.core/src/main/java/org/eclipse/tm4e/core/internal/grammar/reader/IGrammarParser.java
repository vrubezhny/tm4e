package org.eclipse.tm4e.core.internal.grammar.reader;

import java.io.InputStream;

import org.eclipse.tm4e.core.internal.types.IRawGrammar;

public interface IGrammarParser {

	IRawGrammar parse(InputStream contents) throws Exception;
}
