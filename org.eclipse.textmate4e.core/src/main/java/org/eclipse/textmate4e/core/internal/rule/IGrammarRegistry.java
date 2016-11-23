package org.eclipse.textmate4e.core.internal.rule;

import org.eclipse.textmate4e.core.internal.types.IRawGrammar;
import org.eclipse.textmate4e.core.internal.types.IRawRepository;

public interface IGrammarRegistry {

	IRawGrammar getExternalGrammar(String scopeName, IRawRepository repository);
}
