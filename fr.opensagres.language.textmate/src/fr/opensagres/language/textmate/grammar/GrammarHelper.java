package fr.opensagres.language.textmate.grammar;

import fr.opensagres.language.textmate.types.IRawGrammar;

public class GrammarHelper {

	public static IGrammar createGrammar(IRawGrammar rawGrammar, IGrammarRepository repository) {
		return new Grammar(rawGrammar, repository);
	}

	public static String[] extractIncludedScopes(IRawGrammar grammar) {
		// TODO Auto-generated method stub
		return null;
	}
}
