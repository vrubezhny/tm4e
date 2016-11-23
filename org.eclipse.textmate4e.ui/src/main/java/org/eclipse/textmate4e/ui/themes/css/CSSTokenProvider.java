package org.eclipse.textmate4e.ui.themes.css;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.textmate4e.core.theme.IStyle;
import org.eclipse.textmate4e.core.theme.RGB;
import org.eclipse.textmate4e.core.theme.css.CSSParser;
import org.eclipse.textmate4e.ui.themes.AbstractTokenProvider;
import org.eclipse.textmate4e.ui.themes.ColorManager;

public class CSSTokenProvider extends AbstractTokenProvider {

	private Map<IStyle, IToken> tokenMaps;
	private CSSParser parser;

	public CSSTokenProvider(InputStream in) {
		ColorManager manager = new ColorManager();
		tokenMaps = new HashMap<>();
		try {
			parser = new CSSParser(in);
			for (IStyle style : parser.getStyles()) {
				RGB color = style.getColor();
				if (color != null) {
					int s = SWT.NORMAL;
					if (style.isBold()) {
						s = s | SWT.BOLD;
					}
					if (style.isItalic()) {
						s = s | SWT.ITALIC;
					}
					tokenMaps.put(style, new Token(new TextAttribute(manager.getColor(color), null, s)));
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public IToken getToken(String type) {
		if (type == null) {
			return null;
		}
		IStyle style = parser.getBestStyle(Arrays.asList(type.split("[.]")));
		if (style != null) {
			IToken t = tokenMaps.get(style);
			if (t != null) {
				return t;
			}
		}
		return null;
	}

}
