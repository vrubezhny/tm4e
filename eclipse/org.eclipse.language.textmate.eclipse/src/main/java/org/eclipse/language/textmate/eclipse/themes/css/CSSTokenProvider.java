package org.eclipse.language.textmate.eclipse.themes.css;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.language.textmate.core.css.CSSParser;
import org.eclipse.language.textmate.core.css.CSSStyle;
import org.eclipse.language.textmate.eclipse.themes.AbstractTokenProvider;
import org.eclipse.language.textmate.eclipse.themes.ColorManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;

public class CSSTokenProvider extends AbstractTokenProvider {

	private Map<CSSStyle, IToken> tokenMaps;
	private CSSParser parser;

	public CSSTokenProvider(InputStream in) {
		ColorManager manager = new ColorManager();
		tokenMaps = new HashMap<>();
		try {
			parser = new CSSParser(in);
			for (CSSStyle style : parser.getStyles()) {
				RGBColor color = style.getColor();
				if (color != null) {
					RGB rgb = toRGB(color);
					int s = SWT.NORMAL;
					if (style.isBold()) {
						s = s | SWT.BOLD;
					}
					if (style.isItalic()) {
						s = s | SWT.ITALIC;
					}
					tokenMaps.put(style, new Token(new TextAttribute(manager.getColor(rgb), null, s)));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private RGB toRGB(RGBColor rgbColor) {
		if (rgbColor == null)
			return null;
		int green = ((int) rgbColor.getGreen().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
		int red = ((int) rgbColor.getRed().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
		int blue = ((int) rgbColor.getBlue().getFloatValue(CSSPrimitiveValue.CSS_NUMBER));
		return new RGB(red, green, blue);
	}

	@Override
	public IToken getToken(String type) {
		if (type == null) {
			return null;
		}
		CSSStyle style = parser.getBestStyle(Arrays.asList(type.split("[.]")));
		if (style != null) {
			IToken t = tokenMaps.get(style);
			if (t != null) {
				return t;
			}
		}
		return null;
	}

}
