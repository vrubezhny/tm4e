package org.eclipse.textmate4e.core.css;

import java.util.Arrays;
import java.util.List;

import org.eclipse.textmate4e.core.theme.IStyle;
import org.eclipse.textmate4e.core.theme.css.CSSParser;

public class CSSParserTest {

	public static void main(String[] args) throws Exception {
		CSSParser parser = new CSSParser(".comment {color:rgb(0,1,2)} .comment.ts {color:rgb(0,1,2)}");
		List<String> names = Arrays.asList("comment".split("[.]"));
		parser.getBestStyle(names);
		
		
		names = Arrays.asList("comment.ts".split("[.]"));
		IStyle style = parser.getBestStyle(names);
		
		System.err.println(style.getColor().red);
	}
}
