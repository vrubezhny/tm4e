package org.eclipse.textmate4e.core.grammar;

import org.eclipse.textmate4e.core.internal.types.IRawGrammar;

public interface IGrammarRepository {

	/**
	 * Lookup a raw grammar.
	 */
	IRawGrammar lookup(String scopeName);
}
