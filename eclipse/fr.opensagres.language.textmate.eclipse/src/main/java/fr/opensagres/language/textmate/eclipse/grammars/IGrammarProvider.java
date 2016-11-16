package fr.opensagres.language.textmate.eclipse.grammars;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

import fr.opensagres.language.textmate.core.grammar.IGrammar;

public interface IGrammarProvider {

	IGrammar getGrammarFor(IPath location);
}
