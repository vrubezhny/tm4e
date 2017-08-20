package org.eclipse.tm4e.languageconfiguration.internal;

import java.io.Reader;
import java.util.List;

import com.google.gson.GsonBuilder;

public class LanguageConfiguration {

	public static LanguageConfiguration load(Reader json) {
		return new GsonBuilder().create().fromJson(json, LanguageConfiguration.class);
	}

	/**
	 * The language's auto closing pairs. The 'close' character is autautomatically
	 * inserted with the 'open' character is typed. If not set, the configured
	 * brackets will be used.
	 */
	private List<AutoClosingPairConditional> autoClosingPairs;

	/**
	 * The language's surrounding pairs. When the 'open' character is typed on a
	 * selection, the selected string is surrounded by the open and close
	 * characters. If not set, the autoclosing pairs settings will be used.
	 */
	//private List<AutoClosingPair> surroundingPairs;

	public List<AutoClosingPairConditional> getAutoClosingPairs() {
		return autoClosingPairs;
	}

//	public List<AutoClosingPair> getSurroundingPairs() {
//		return surroundingPairs;
//	}
}
