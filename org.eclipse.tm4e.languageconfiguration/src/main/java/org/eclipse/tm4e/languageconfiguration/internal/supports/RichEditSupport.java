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

import org.eclipse.tm4e.languageconfiguration.internal.LanguageConfiguration;
import org.eclipse.tm4e.languageconfiguration.internal.LanguageConfigurationDefinition;

/**
 * Support for Editor.
 *
 */
public class RichEditSupport {

	private LanguageConfigurationDefinition configuration;

	private CharacterPairSupport characterPair;

	private OnEnterSupport onEnter;

	public RichEditSupport(String contentTypeId, RichEditSupport previous,
			LanguageConfigurationDefinition configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns the "character pair" support and null otherwise.
	 * 
	 * @return the "character pair" support and null otherwise.
	 */
	public CharacterPairSupport getCharacterPair() {
		if (this.characterPair == null) {
			LanguageConfiguration conf = configuration.getLanguageConfiguration();
			this.characterPair = new CharacterPairSupport(conf.getBrackets(), conf.getAutoClosingPairs(),
					conf.getSurroundingPairs());
		}
		return characterPair;
	}

	/**
	 * Returns the "on enter" support and null otherwise.
	 * 
	 * @return the "on enter" support and null otherwise.
	 */
	public OnEnterSupport getOnEnter() {
		if (this.onEnter == null) {
			LanguageConfiguration conf = configuration.getLanguageConfiguration();
			if (conf.getBrackets() != null || conf.getOnEnterRules() != null) {
				this.onEnter = new OnEnterSupport(conf.getBrackets(), conf.getOnEnterRules());
			}
		}
		return onEnter;
	}
}
