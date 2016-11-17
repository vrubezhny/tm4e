package org.eclipse.language.textmate.eclipse.grammars;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.language.textmate.core.grammar.IGrammar;

public interface IGrammarRegistryManager {

	IGrammar getGrammarFor(IFile file) throws CoreException;
}
