package org.eclipse.language.textmate.core.css;

import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.RGBColor;

public class CSSStyle {

	private final SelectorList selector;
	private RGBColor color;
	private boolean bold;
	private boolean italic;

	public CSSStyle(SelectorList selector) {
		this.selector = selector;
	}

	public void setColor(RGBColor color) {
		this.color = color;
	}

	public RGBColor getColor() {
		return color;
	}

	public SelectorList getSelectorList() {
		return selector;
	}

	public String getName() {
		return "a";
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isBold() {
		return bold;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public boolean isItalic() {
		return italic;
	}
}
