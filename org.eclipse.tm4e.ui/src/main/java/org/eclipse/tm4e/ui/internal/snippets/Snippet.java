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
package org.eclipse.tm4e.ui.internal.snippets;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.tm4e.registry.TMResource;
import org.eclipse.tm4e.registry.XMLConstants;
import org.eclipse.tm4e.ui.snippets.ISnippet;

public class Snippet extends TMResource implements ISnippet {

	private String scopeName;
	private String name;

	/**
	 * Constructor for user preferences (loaded from Json with Gson).
	 */
	public Snippet() {
		super();
	}

	/**
	 * Constructor for extension point.
	 * 
	 * @param element
	 */
	public Snippet(String scopeName, String path, String name) {
		super(path);
		this.scopeName = scopeName;
		this.name = name;
	}

	public Snippet(IConfigurationElement ce) {
		super(ce);
		this.scopeName = ce.getAttribute(XMLConstants.SCOPE_NAME_ATTR);
		this.name = ce.getAttribute(XMLConstants.NAME_ATTR);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getContent() {
		return getResourceContent();
	}

	@Override
	public String getScopeName() {
		return scopeName;
	}
}
