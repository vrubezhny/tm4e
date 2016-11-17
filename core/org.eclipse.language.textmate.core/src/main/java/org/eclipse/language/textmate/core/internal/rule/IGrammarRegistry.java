package org.eclipse.language.textmate.core.internal.rule;

import org.eclipse.language.textmate.core.internal.types.IRawGrammar;
import org.eclipse.language.textmate.core.internal.types.IRawRepository;

public interface IGrammarRegistry {

	IRawGrammar getExternalGrammar(String scopeName, IRawRepository repository);
}
