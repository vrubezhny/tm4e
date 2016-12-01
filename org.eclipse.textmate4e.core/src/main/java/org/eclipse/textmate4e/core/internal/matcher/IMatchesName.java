package org.eclipse.textmate4e.core.internal.matcher;

import java.util.Collection;
import java.util.List;

import org.eclipse.textmate4e.core.grammar.StackElement;

public interface IMatchesName<T> {

	public static final IMatchesName<StackElement> NAME_MATCHER = new IMatchesName<StackElement>() {

		@Override
		public boolean match(Collection<String> identifers, StackElement stackElements) {
			List<String> scopes = stackElements.generateScopes();
			int lastIndex = 0;
			// every
			for (String identifier : identifers) {
				lastIndex = match(identifier, scopes, lastIndex);
				if (lastIndex == -1) {
					return false;
				}
			}
			return true;
		}

		private int match(String identifier, List<String> scopes, int lastIndex) {
			for (int i = lastIndex; i < scopes.size(); i++) {
				if (scopesAreMatching(scopes.get(i), identifier)) {
					return i;
				}
			}
			return -1;
		}

		private boolean scopesAreMatching(String thisScopeName, String scopeName) {
			if (thisScopeName == null) {
				return false;
			}
			if (thisScopeName.equals(scopeName)) {
				return true;
			}
			int len = scopeName.length();
			return thisScopeName.length() > len && thisScopeName.substring(0, len).equals(scopeName)
					&& thisScopeName.charAt(len) == '.';
		}

	};

	boolean match(Collection<String> names, T matcherInput);

}