/**
 *  Copyright (c) 2018 Red Hat Inc. and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Lucas Bullen (Red Hat Inc.) - initial API and implementation
 */
package org.eclipse.tm4e.languageconfiguration;

import org.eclipse.core.runtime.content.IContentType;
import org.osgi.service.prefs.BackingStoreException;

/**
 *
 * Language Configuration registry manager API.
 *
 */
public interface ILanguageConfigurationRegistryManager {

	// --------------- Language configuration definitions methods

	/**
	 * Returns the list of registered language configuration definitions.
	 *
	 * @return the list of registered language configuration definitions.
	 */
	ILanguageConfigurationDefinition[] getDefinitions();

	/**
	 * Add language configuration definition to the registry.
	 *
	 * NOTE: you must call save() method if you wish to save in the preferences.
	 *
	 * @param definition
	 */
	void registerLanguageConfigurationDefinition(ILanguageConfigurationDefinition definition);

	/**
	 * Remove language configuration definition from the registry.
	 *
	 * NOTE: you must call save() method if you wish to save in the preferences.
	 *
	 * @param definition
	 */
	void unregisterLanguageConfigurationDefinition(ILanguageConfigurationDefinition definition);

	/**
	 * Save the language configuration definitions.
	 *
	 * @throws BackingStoreException
	 */
	void save() throws BackingStoreException;

	// --------------- Language configuration queries methods.

	/**
	 * Returns the {@link ILanguageConfiguration} for the given content types and
	 * null otherwise.
	 *
	 * @param contentTypes the content type.
	 * @return the {@link ILanguageConfiguration} for the given content type and
	 *         null otherwise.
	 */
	ILanguageConfiguration getLanguageConfigurationFor(IContentType[] contentTypes);

}
