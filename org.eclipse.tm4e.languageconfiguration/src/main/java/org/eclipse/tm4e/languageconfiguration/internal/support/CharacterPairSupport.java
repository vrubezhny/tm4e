package org.eclipse.tm4e.languageconfiguration.internal.support;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.tm4e.languageconfiguration.internal.IAutoClosingPair;
import org.eclipse.tm4e.languageconfiguration.internal.LanguageConfiguration;
import org.eclipse.tm4e.languageconfiguration.internal.StandardAutoClosingPairConditional;

public class CharacterPairSupport {

	private List<IAutoClosingPair> autoClosingPairs;

	public CharacterPairSupport(LanguageConfiguration config) {
		if (config.getAutoClosingPairs() != null) {
			this.autoClosingPairs = config.getAutoClosingPairs().stream()
					.map(el -> new StandardAutoClosingPairConditional(el)).collect(Collectors.toList());
		} else {
			this.autoClosingPairs = new ArrayList<>();
		}
	}

	public boolean shouldAutoClosePair(String character/* : string, context: ScopedLineTokens, column: number */) {
		for (IAutoClosingPair autoClosingPair : autoClosingPairs) {
			if (character.equals(autoClosingPair.getOpen())) {
				return true;
			}
		}
		return false;
	}

	public List<IAutoClosingPair> getAutoClosingPairs() {
		return autoClosingPairs;
	}
}
