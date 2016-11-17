package org.eclipse.language.textmate.eclipse.grammars;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.language.textmate.core.grammar.IGrammar;

public interface IGrammarProvider {

	IGrammar getGrammarFor(IPath location);
}
