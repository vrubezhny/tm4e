package fr.opensagres.language.textmate.core.internal.rule;

import fr.opensagres.language.textmate.core.internal.types.IRawGrammar;
import fr.opensagres.language.textmate.core.internal.types.IRawRepository;

public interface IGrammarRegistry {

	IRawGrammar getExternalGrammar(String scopeName, IRawRepository repository);
}
