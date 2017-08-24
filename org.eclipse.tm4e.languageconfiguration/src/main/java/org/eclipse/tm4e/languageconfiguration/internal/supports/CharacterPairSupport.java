/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.languageconfiguration.internal.supports;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The "character pair" support.
 *
 */
public class CharacterPairSupport {

	private List<AutoClosingPair> autoClosingPairs;
	private List<AutoClosingPair> surroundingPairs;

	public CharacterPairSupport(List<CharacterPair> brackets, List<AutoClosingPairConditional> autoClosingPairs,
			List<AutoClosingPair> surroundingPairs) {
		if (autoClosingPairs != null) {
			this.autoClosingPairs = autoClosingPairs.stream()
					.map(el -> new StandardAutoClosingPairConditional(el.getOpen(), el.getClose(), el.getNotIn()))
					.collect(Collectors.toList());
		} else if (brackets != null) {
			this.autoClosingPairs = brackets.stream()
					.map(el -> new StandardAutoClosingPairConditional(el.getKey(), el.getValue(), null))
					.collect(Collectors.toList());
		} else {
			this.autoClosingPairs = new ArrayList<>();
		}

		this.surroundingPairs = surroundingPairs != null ? surroundingPairs : this.autoClosingPairs;
	}

	public boolean shouldAutoClosePair(String character/* : string, context: ScopedLineTokens, column: number */) {
		for (AutoClosingPair autoClosingPair : autoClosingPairs) {
			if (character.equals(autoClosingPair.getOpen())) {
				return true;
			}
		}
		return false;
	}

	public List<AutoClosingPair> getAutoClosingPairs() {
		return autoClosingPairs;
	}
}
