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
package org.eclipse.tm4e.core;

import org.eclipse.tm4e.core.internal.oniguruma.IOnigNextMatchResult;
import org.eclipse.tm4e.core.internal.oniguruma.OnigScanner;

public class TestOngurama {

	public static void main(String[] args) {

		OnigScanner scanner = new OnigScanner(new String[] { "c", "a(b)?" });
		IOnigNextMatchResult result = scanner._findNextMatchSync("abc", 0);
		System.err.println(result);

		scanner = new OnigScanner(new String[] { "a([b-d])c" });
		result = scanner._findNextMatchSync("!abcdef", 0);
		System.err.println(result);
	}
}
