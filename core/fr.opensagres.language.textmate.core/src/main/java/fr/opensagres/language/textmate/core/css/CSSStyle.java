package fr.opensagres.language.textmate.core.css;

import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.RGBColor;

public class CSSStyle {

	private final SelectorList selector;
	private RGBColor color;
	
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

}
