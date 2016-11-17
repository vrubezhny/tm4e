package org.eclipse.language.textmate.core.internal.grammar.parser.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.language.textmate.core.internal.grammar.parser.PList;
import org.eclipse.language.textmate.core.internal.grammar.reader.IGrammarParser;
import org.eclipse.language.textmate.core.internal.types.IRawGrammar;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLPListParser implements IGrammarParser {

	public final static IGrammarParser INSTANCE = new XMLPListParser();

	@Override
	public IRawGrammar parse(InputStream contents) throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setEntityResolver(new EntityResolver() {

			@Override
			public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
				return new InputSource(new ByteArrayInputStream("<?xml version='1.0' encoding='UTF-8'?>".getBytes()));
			}
		});
		PList result = new PList();
		xmlReader.setContentHandler(result);
		xmlReader.parse(new InputSource(contents));
		return result.getResult();
	}
}
