package org.eclipse.language.textmate.eclipse.grammars;

import org.eclipse.core.runtime.IPath;
import org.eclipse.language.textmate.core.grammar.IGrammar;

public abstract class AbstractGrammarProvider implements IGrammarProvider {

	@Override
	public IGrammar getGrammarFor(IPath location) {
		if (!isAvailableFor(location)) {
			return null;
		}
		return null;
	}

	protected abstract boolean isAvailableFor(IPath resource);
}
