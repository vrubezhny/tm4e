package org.eclipse.language.textmate.core.grammar;

import org.eclipse.language.textmate.core.internal.types.IRawGrammar;

public interface IGrammarRepository {

	/**
	 * Lookup a raw grammar.
	 */
	IRawGrammar lookup(String scopeName);
}
