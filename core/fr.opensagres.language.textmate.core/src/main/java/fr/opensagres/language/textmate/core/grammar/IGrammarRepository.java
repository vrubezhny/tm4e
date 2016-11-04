package fr.opensagres.language.textmate.core.grammar;

import fr.opensagres.language.textmate.core.internal.types.IRawGrammar;

public interface IGrammarRepository {

	/**
	 * Lookup a raw grammar.
	 */
	IRawGrammar lookup(String scopeName);
}
