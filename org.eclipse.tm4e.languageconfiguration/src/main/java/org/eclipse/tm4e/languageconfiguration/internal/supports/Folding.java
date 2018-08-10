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

public class Folding {

	private final Boolean offSide;

	private final String markersStart;

	private final String markersEnd;

	public Folding(Boolean offSide, String markersStart, String markersEnd) {
		this.offSide = offSide;
		this.markersStart = markersStart;
		this.markersEnd = markersEnd;
	}

	public Boolean getOffSide() {
		return offSide;
	}

	public String getMarkersStart() {
		return markersStart;
	}

	public String getMarkersEnd() {
		return markersEnd;
	}
}