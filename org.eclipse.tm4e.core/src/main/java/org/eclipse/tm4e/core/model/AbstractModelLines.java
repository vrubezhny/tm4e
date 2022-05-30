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

import static java.lang.System.Logger.Level.*;

import java.lang.System.Logger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tm4e.core.grammar.IStateStack;

/**
 * Abstract class for Model lines used by the TextMate model.
 *
 * Implementation class must:
 * <ul>
 * <li>synchronizes lines with the lines of the editor content when it changed.</li>
 * <li>call {@link AbstractModelLines#invalidateLine(int)} with the first changed line.</li>
 * </ul>
 */
public abstract class AbstractModelLines implements IModelLines {

	static final class ModelLine {
		/**
		 * specifies if the tokens are out-of-date and do not represent the content of the related line in the editor
		 */
		volatile boolean isInvalid = true;
		@Nullable
		IStateStack startState;
		List<TMToken> tokens = Collections.emptyList();
	}

	private static final Logger LOGGER = System.getLogger(AbstractModelLines.class.getName());

	private final List<ModelLine> list = Collections.synchronizedList(new LinkedList<>());

	@Nullable
	private TMModel model;

	protected void setModel(@Nullable final TMModel model) {
		this.model = model;
		list.forEach(line -> line.isInvalid = true);
	}

	@Override
	public void addLine(final int line) {
		try {
			this.list.add(line, new ModelLine());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeLine(final int line) {
		this.list.remove(line);
	}

	@Override
	public void updateLine(final int line) {
		try {
			// TODO this.list.get(line).text = this.lineToTextResolver.apply(line);
		} catch (final Exception ex) {
			LOGGER.log(ERROR, ex.getMessage(), ex);
		}
	}

	ModelLine get(final int index) {
		return list.get(index);
	}

	@Nullable
	ModelLine getOrNull(final int index) {
		try {
			return list.get(index);
		} catch (final IndexOutOfBoundsException ex) {
			return null;
		}
	}

	@Override
	public boolean hasLine(final int lineIndex) {
		return lineIndex > -1 && lineIndex < list.size();
	}

	protected void invalidateLine(final int lineIndex) {
		if (model != null) {
			model.invalidateLine(lineIndex);
		}
	}
}