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
package org.eclipse.tm4e.languageconfiguration.internal.supports;

public class Comments {

	private final String lineComment;

	private final CharacterPair blockComment;

	public Comments(String lineComment, CharacterPair blockComment) {
		this.lineComment = lineComment;
		this.blockComment = blockComment;
	}

	public String getLineComment() {
		return lineComment;
	}

	public CharacterPair getBlockComment() {
		return blockComment;
	}
}