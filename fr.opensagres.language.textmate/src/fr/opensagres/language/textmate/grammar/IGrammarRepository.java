package fr.opensagres.language.textmate.grammar;

import fr.opensagres.language.textmate.types.IRawGrammar;

public interface IGrammarRepository {

	/**
	 * Lookup a raw grammar.
	 */
	IRawGrammar lookup(String scopeName);
}
