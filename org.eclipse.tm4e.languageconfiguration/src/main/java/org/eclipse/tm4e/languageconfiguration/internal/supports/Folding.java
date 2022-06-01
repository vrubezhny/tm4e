/**
 *  Copyright (c) 2018 Red Hat Inc. and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *  Contributors:
 *  Lucas Bullen (Red Hat Inc.) - initial API and implementation
 */
package org.eclipse.tm4e.languageconfiguration.internal.supports;

public final class Folding {

	public final boolean offSide;
	public final String markersStart;
	public final String markersEnd;

	public Folding(final boolean offSide, final String markersStart, final String markersEnd) {
		this.offSide = offSide;
		this.markersStart = markersStart;
		this.markersEnd = markersEnd;
	}
}