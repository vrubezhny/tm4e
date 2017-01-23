/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 * This code is an translation of code copyrighted by Microsoft Corporation, and initially licensed under MIT.
 *
 * Contributors:
 *  - Microsoft Corporation: Initial code, written in TypeScript, licensed under MIT license
 *  - Angelo Zerr <angelo.zerr@gmail.com> - translation and adaptation to Java
 */
package org.eclipse.tm4e.core.internal.grammar.reader;

import java.io.InputStream;

import org.eclipse.tm4e.core.internal.grammar.parser.json.JSONPListParser;
import org.eclipse.tm4e.core.internal.grammar.parser.xml.XMLPListParser;
import org.eclipse.tm4e.core.internal.types.IRawGrammar;

public class GrammarReader {

	public static IRawGrammar readGrammarSync(String filePath, InputStream in) throws Exception {
		SyncGrammarReader reader = new SyncGrammarReader(in, getGrammarParser(filePath));
		return reader.load();
	}

	private static IGrammarParser getGrammarParser(String filePath) {
		if (filePath.endsWith(".json")) {
			return JSONPListParser.INSTANCE;
		}
		return XMLPListParser.INSTANCE;
	}
}
