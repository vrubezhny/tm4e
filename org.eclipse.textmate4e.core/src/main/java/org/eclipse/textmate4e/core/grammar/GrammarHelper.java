package org.eclipse.textmate4e.core.grammar;

import org.eclipse.textmate4e.core.internal.grammar.Grammar;
import org.eclipse.textmate4e.core.internal.oniguruma.OnigString;
import org.eclipse.textmate4e.core.internal.types.IRawGrammar;

public class GrammarHelper {

	public static IGrammar createGrammar(IRawGrammar rawGrammar, IGrammarRepository repository) {
		return new Grammar(rawGrammar, repository);
	}

	public static OnigString createOnigString(String str) {
		return new OnigString(str);
	}

}
