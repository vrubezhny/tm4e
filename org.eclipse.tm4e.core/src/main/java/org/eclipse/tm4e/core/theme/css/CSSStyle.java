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
package org.eclipse.tm4e.core.theme.css;

import org.eclipse.tm4e.core.theme.IStyle;
import org.eclipse.tm4e.core.theme.RGB;
import org.w3c.css.sac.SelectorList;

public class CSSStyle implements IStyle {

	private final SelectorList selector;
	private RGB color;
	private boolean bold;
	private boolean italic;

	public CSSStyle(SelectorList selector) {
		this.selector = selector;
	}

	public void setColor(RGB color) {
		this.color = color;
	}

	@Override
	public RGB getColor() {
		return color;
	}

	public SelectorList getSelectorList() {
		return selector;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	@Override
	public boolean isBold() {
		return bold;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	@Override
	public boolean isItalic() {
		return italic;
	}
}
