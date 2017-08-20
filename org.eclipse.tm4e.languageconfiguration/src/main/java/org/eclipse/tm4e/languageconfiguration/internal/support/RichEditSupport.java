package org.eclipse.tm4e.languageconfiguration.internal.support;

import org.eclipse.tm4e.languageconfiguration.internal.LanguageConfigurationDelegate;

public class RichEditSupport {

	private LanguageConfigurationDelegate configuration;
	private CharacterPairSupport characterPair;
	
	public RichEditSupport(String contentTypeId, RichEditSupport previous, LanguageConfigurationDelegate configuration) {
		this.configuration = configuration;
		
		//this.characterPair = new CharacterPairSupport(configuration);
	}
	public CharacterPairSupport getCharacterPair() {
		if (this.characterPair == null) {
			this.characterPair = new CharacterPairSupport(configuration.getLanguageConfiguration());
		}
		return characterPair;
	}
}
