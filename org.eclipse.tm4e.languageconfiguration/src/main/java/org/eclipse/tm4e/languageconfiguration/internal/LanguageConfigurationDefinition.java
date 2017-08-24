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

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tm4e.registry.TMResource;
import org.eclipse.tm4e.registry.XMLConstants;

/**
 * Language configuration definition.
 *
 */
public class LanguageConfigurationDefinition extends TMResource {

	private String contentTypeId;

	public LanguageConfigurationDefinition(IConfigurationElement ce) {
		super(ce);
		this.contentTypeId = ce.getAttribute(XMLConstants.CONTENT_TYPE_ID_ATTR);
	}

	public String getContentTypeId() {
		return contentTypeId;
	}

	public LanguageConfiguration getLanguageConfiguration() {
		try {
			return LanguageConfiguration.load(new InputStreamReader(getInputStream(), Charset.defaultCharset()));
		} catch (IOException e) {
			// TODO: log it!!!
			return null;
		}
	}

}
