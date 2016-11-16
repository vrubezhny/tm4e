package fr.opensagres.language.textmate.eclipse.grammars;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import fr.opensagres.language.textmate.core.grammar.IGrammar;

public interface IGrammarRegistryManager {

	IGrammar getGrammarFor(IFile file) throws CoreException;
}
