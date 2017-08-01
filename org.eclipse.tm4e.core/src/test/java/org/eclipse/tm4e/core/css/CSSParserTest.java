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
package org.eclipse.tm4e.core.css;

import org.eclipse.tm4e.core.theme.IStyle;
import org.eclipse.tm4e.core.theme.css.CSSParser;

public class CSSParserTest {

	public static void main(String[] args) throws Exception {
		CSSParser parser = new CSSParser(".comment {color:rgb(0,1,2)} .comment.ts {color:rgb(0,1,2)}");
		String[] names = "comment".split("[.]");
		parser.getBestStyle(names);
		
		names = "comment.ts".split("[.]");
		IStyle style = parser.getBestStyle(names);
		
		System.err.println(style.getColor().red);
	}
}
