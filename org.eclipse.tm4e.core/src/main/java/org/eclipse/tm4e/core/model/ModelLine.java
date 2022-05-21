/**
 * Copyright (c) 2015-2017 Angelo ZERR.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.core.model;

import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tm4e.core.grammar.IStateStack;

public class ModelLine {

	protected boolean isInvalid;

	@Nullable
	protected IStateStack state;

	@Nullable
	protected List<TMToken> tokens;

	public void resetTokenizationState() {
		this.state = null;
		this.tokens = null;
	}

	@Nullable
	public IStateStack getState() {
		return state;
	}

	@Nullable
	public List<TMToken> getTokens() {
		return tokens;
	}
}
