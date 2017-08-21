package org.eclipse.tm4e.languageconfiguration.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.tm4e.languageconfiguration.internal.support.CharacterPairSupport;
import org.eclipse.tm4e.languageconfiguration.internal.support.RichEditSupport;

public class LanguageConfigurationRegistry {

	private static final String EXTENSION_LANGUAGE_CONFIGURATIONS = "languageConfigurations"; //$NON-NLS-1$
	private static final String LANGUAGE_CONFIGURATION_ELT = "languageConfiguration"; //$NON-NLS-1$

	private static LanguageConfigurationRegistry INSTANCE;

	public static LanguageConfigurationRegistry getInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		INSTANCE = createInstance();
		return INSTANCE;
	}

	private static synchronized LanguageConfigurationRegistry createInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		LanguageConfigurationRegistry manager = new LanguageConfigurationRegistry();
		manager.load();
		return manager;
	}

	private void load() {
		loadFromExtensionPoints();
	}

	private void loadFromExtensionPoints() {
		IConfigurationElement[] cf = Platform.getExtensionRegistry().getConfigurationElementsFor(LanguageConfigurationPlugin.PLUGIN_ID,
				EXTENSION_LANGUAGE_CONFIGURATIONS);
		for (IConfigurationElement ce : cf) {
			String name = ce.getName();
			if (LANGUAGE_CONFIGURATION_ELT.equals(name)) {
				LanguageConfigurationDelegate delegate = new LanguageConfigurationDelegate(ce);				
				register(delegate, delegate.getContentTypeId());
			}
		}
		
	}

	private final Map<String, RichEditSupport> supports;

	public LanguageConfigurationRegistry() {
		this.supports = new HashMap<>();
	}

	private CharacterPairSupport _getCharacterPairSupport(String contentTypeId) {
		RichEditSupport value = this.getRichEditSupport(contentTypeId);
		if (value == null) {
			return null;
		}
		return value.getCharacterPair();
	}

	public void register(LanguageConfigurationDelegate configuration, String contentTypeId) {
		RichEditSupport current = new RichEditSupport(contentTypeId, null, configuration);
		this.supports.put(contentTypeId, current);
	}

	private RichEditSupport getRichEditSupport(String contentTypeId) {
		return supports.get(contentTypeId);
	}

	public List<IAutoClosingPair> getAutoClosingPairs(String contentTypeId) {
		CharacterPairSupport characterPairSupport = this._getCharacterPairSupport(contentTypeId);
		if (characterPairSupport == null) {
			return Collections.emptyList();
		}
		return characterPairSupport.getAutoClosingPairs();
	}

	public boolean shouldAutoClosePair(String character,
			String contentTypeId/* , context: LineTokens, column: number */) {
		// let scopedLineTokens = createScopedLineTokens(context, column - 1);
		CharacterPairSupport characterPairSupport = this
				._getCharacterPairSupport(contentTypeId /* scopedLineTokens.languageId */);
		if (characterPairSupport == null) {
			return false;
		}
		return characterPairSupport
				.shouldAutoClosePair(character/* , scopedLineTokens, column - scopedLineTokens.firstCharOffset */);
	}
}
