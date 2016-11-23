package org.eclipse.textmate4e.core.theme.css;

import org.eclipse.textmate4e.core.theme.IStyle;
import org.eclipse.textmate4e.core.theme.RGB;
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
