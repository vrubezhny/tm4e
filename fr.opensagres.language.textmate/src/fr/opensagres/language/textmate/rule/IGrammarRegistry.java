package fr.opensagres.language.textmate.rule;

import fr.opensagres.language.textmate.types.IRawGrammar;
import fr.opensagres.language.textmate.types.IRawRepository;

public interface IGrammarRegistry {

	IRawGrammar getExternalGrammar(String scopeName, IRawRepository repository);
}
