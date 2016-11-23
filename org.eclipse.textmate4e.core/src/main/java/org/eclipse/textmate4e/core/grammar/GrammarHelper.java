package org.eclipse.textmate4e.core.grammar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.textmate4e.core.internal.types.IRawGrammar;
import org.eclipse.textmate4e.core.internal.types.IRawRepository;
import org.eclipse.textmate4e.core.internal.types.IRawRule;

public class GrammarHelper {

	public static IGrammar createGrammar(IRawGrammar rawGrammar, IGrammarRepository repository) {
		return new Grammar(rawGrammar, repository);
	}

	/**
	 * Fill in `result` all external included scopes in `patterns`
	 */
	private static void _extractIncludedScopesInPatterns(Collection<String> result, Collection<IRawRule> patterns) {
		for (IRawRule pattern : patterns) {
			Collection<IRawRule> p = pattern.getPatterns();
			if (p != null) {
				_extractIncludedScopesInPatterns(result, p);
			}
			
			String include = pattern.getInclude();
			if (include == null) {
				continue;
			}
			
			if (include.equals("$base") || include.equals("$self")) {
				// Special includes that can be resolved locally in this grammar
				continue;
			}
			
			if (include.charAt(0) == '#') {
				// Local include from this grammar
				continue;
			}
			
			int sharpIndex = include.indexOf('#');
			if (sharpIndex >= 0) {
				//result[include.substring(0, sharpIndex)] = true;
				result.add(include.substring(0, sharpIndex));
			} else {
				result.add(include);
				//result[include] = true;
			}
		}
	}

	/**
	 * Fill in `result` all external included scopes in `repository`
	 */
	private static void _extractIncludedScopesInRepository(Collection<String> result, IRawRepository repository) {
		Map<String, Object> r = (Map<String, Object>) repository;
		for (Entry<String, Object> entry : r.entrySet()) {
			IRawRule rule = (IRawRule) entry.getValue();
			if (rule.getPatterns() != null) {
				_extractIncludedScopesInPatterns(result, rule.getPatterns());
			}
			if (rule.getRepository() != null) {
				_extractIncludedScopesInRepository(result, rule.getRepository());
			}
		}
//		for (let name in repository) {
//			let rule = repository[name];
//
//			if (rule.patterns && Array.isArray(rule.patterns)) {
//				_extractIncludedScopesInPatterns(result, rule.patterns);
//			}
//
//			if (rule.repository) {
//				_extractIncludedScopesInRepository(result, rule.repository);
//			}
//		}
	}
	
	/**
	 * Return a list of all external included scopes in `grammar`.
	 */
	public static String[] extractIncludedScopes(IRawGrammar grammar) {
		// let result: IScopeNameSet = {};
		Collection<String> result = new ArrayList<String>();
		if (grammar
				.getPatterns() != null /* && Array.isArray(grammar.patterns) */) {
			_extractIncludedScopesInPatterns(result, grammar.getPatterns());
		}

		if (grammar.getRepository() != null) {
			_extractIncludedScopesInRepository(result, grammar.getRepository());
		}

		// remove references to own scope (avoid recursion)
		// delete result[grammar.scopeName];
		result.remove(grammar.getScopeName());

		// return Object.keys(result);
		return result.toArray(new String[0]);
	}

}
