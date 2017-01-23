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
package org.eclipse.tm4e.core.internal.grammar.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PListObject {

	private final PListObject parent;
	private final List<Object> arrayValues;
	private final Map<String, Object> mapValues;

	private String lastKey;

	public PListObject(PListObject parent, boolean valueAsArray) {
		this.parent = parent;
		if (valueAsArray) {
			this.arrayValues = new ArrayList<Object>();
			this.mapValues = null;
		} else {
			this.arrayValues = null;
			this.mapValues = new Raw();
		}
	}

	public PListObject getParent() {
		return parent;
	}

	public String getLastKey() {
		return lastKey;
	}

	public void setLastKey(String lastKey) {
		this.lastKey = lastKey;
	}

	public void addValue(Object value) {
		if (isValueAsArray()) {
			arrayValues.add(value);
		} else {
			mapValues.put(getLastKey(), value);
		}
	}
	// Object getValue();

	public boolean isValueAsArray() {
		return arrayValues != null;
	}

	public Object getValue() {
		if (isValueAsArray()) {
			return arrayValues;
		}
		return mapValues;
	}
}
