package org.eclipse.language.textmate.core.internal.grammar.reader;

import java.io.InputStream;

import org.eclipse.language.textmate.core.internal.types.IRawGrammar;

public interface IGrammarParser {

	IRawGrammar parse(InputStream contents) throws Exception;
}
