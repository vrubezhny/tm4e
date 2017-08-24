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
package org.eclipse.tm4e.languageconfiguration.internal;

import java.io.Reader;
import java.util.List;

import org.eclipse.tm4e.languageconfiguration.internal.supports.AutoClosingPair;
import org.eclipse.tm4e.languageconfiguration.internal.supports.AutoClosingPairConditional;
import org.eclipse.tm4e.languageconfiguration.internal.supports.CharacterPair;
import org.eclipse.tm4e.languageconfiguration.internal.supports.OnEnterRule;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

/**
 * VSCode language-configuration.json
 * 
 * @see https://code.visualstudio.com/docs/extensionAPI/extension-points#_contributeslanguages
 */
public class LanguageConfiguration {

	/**
	 * Returns an instance of {@link LanguageConfiguration} loaded from the VSCode
	 * language-configuration.json file reader.
	 * 
	 * @param reader
	 * @return an instance of {@link LanguageConfiguration} loaded from the VSCode
	 *         language-configuration.json file reader.
	 */
	public static LanguageConfiguration load(Reader reader) {
		return new GsonBuilder().registerTypeAdapter(CharacterPair.class,
				(JsonDeserializer<CharacterPair>) (json, typeOfT, context) -> {
					if (json.isJsonArray()) {
						// ex: ["{","}"]
						JsonArray characterPairs = json.getAsJsonArray();
						return new CharacterPair(characterPairs.get(0).getAsString(),
								characterPairs.get(1).getAsString());
					} else {
						// ex: {"open":"'","close":"'"}
						JsonObject object = json.getAsJsonObject();
						return new CharacterPair(object.get("open").getAsString(), object.get("close").getAsString());
					}
				}).registerTypeAdapter(AutoClosingPair.class,
						(JsonDeserializer<AutoClosingPair>) (json, typeOfT, context) -> {
							if (json.isJsonArray()) {
								// ex: ["{","}"]
								JsonArray autoClosingPairs = json.getAsJsonArray();
								return new AutoClosingPair(autoClosingPairs.get(0).getAsString(),
										autoClosingPairs.get(1).getAsString());
							} else {
								// ex: {"open":"'","close":"'"}
								JsonObject object = json.getAsJsonObject();
								return new AutoClosingPair(object.get("open").getAsString(),
										object.get("close").getAsString());
							}
						})
				.registerTypeAdapter(AutoClosingPairConditional.class,
						(JsonDeserializer<AutoClosingPairConditional>) (json, typeOfT, context) -> {
							if (json.isJsonArray()) {
								// ex: ["{","}"]
								JsonArray autoClosingPairs = json.getAsJsonArray();
								return new AutoClosingPairConditional(autoClosingPairs.get(0).getAsString(),
										autoClosingPairs.get(1).getAsString());
							} else {
								// ex: {"open":"'","close":"'"}
								JsonObject object = json.getAsJsonObject();
								return new AutoClosingPairConditional(object.get("open").getAsString(),
										object.get("close").getAsString());
							}
						})
				.create().fromJson(reader, LanguageConfiguration.class);
	}

	/**
	 * The language's brackets. This configuration implicitly affects pressing Enter
	 * around these brackets.
	 */
	private List<CharacterPair> brackets;

	/**
	 * The language's rules to be evaluated when pressing Enter.
	 */
	private List<OnEnterRule> onEnterRules;

	/**
	 * The language's auto closing pairs. The 'close' character is automatically
	 * inserted with the 'open' character is typed. If not set, the configured
	 * brackets will be used.
	 */
	private List<AutoClosingPairConditional> autoClosingPairs;

	/**
	 * The language's surrounding pairs. When the 'open' character is typed on a
	 * selection, the selected string is surrounded by the open and close
	 * characters. If not set, the autoclosing pairs settings will be used.
	 */
	private List<AutoClosingPair> surroundingPairs;

	/**
	 * Returns the language's brackets. This configuration implicitly affects
	 * pressing Enter around these brackets.
	 * 
	 * @return the language's brackets. This configuration implicitly affects
	 *         pressing Enter around these brackets.
	 */
	public List<CharacterPair> getBrackets() {
		return brackets;
	}

	/**
	 * Returns the language's auto closing pairs. The 'close' character is
	 * automatically inserted with the 'open' character is typed. If not set, the
	 * configured brackets will be used.
	 * 
	 * @return the language's auto closing pairs. The 'close' character is
	 *         autautomatically inserted with the 'open' character is typed. If not
	 *         set, the configured brackets will be used.
	 */
	public List<AutoClosingPairConditional> getAutoClosingPairs() {
		return autoClosingPairs;
	}

	/**
	 * Returns the language's rules to be evaluated when pressing Enter.
	 * 
	 * @return the language's rules to be evaluated when pressing Enter.
	 */
	public List<OnEnterRule> getOnEnterRules() {
		return onEnterRules;
	}

	/**
	 * Returns the language's surrounding pairs. When the 'open' character is typed
	 * on a selection, the selected string is surrounded by the open and close
	 * characters. If not set, the autoclosing pairs settings will be used.
	 * 
	 * @return the language's surrounding pairs. When the 'open' character is typed
	 *         on a selection, the selected string is surrounded by the open and
	 *         close characters. If not set, the autoclosing pairs settings will be
	 *         used.
	 */
	public List<AutoClosingPair> getSurroundingPairs() {
		return surroundingPairs;
	}
}
