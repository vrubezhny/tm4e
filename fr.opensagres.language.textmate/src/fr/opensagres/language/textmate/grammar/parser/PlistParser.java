package fr.opensagres.language.textmate.grammar.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import fr.opensagres.language.textmate.grammar.reader.IGrammarParser;
import fr.opensagres.language.textmate.types.IRawGrammar;

public class PlistParser implements IGrammarParser {

	public final static IGrammarParser INSTANCE = new PlistParser();

	@Override
	public IRawGrammar parse(InputStream contents) throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setEntityResolver(new EntityResolver() {
			
			@Override
			public InputSource resolveEntity(String arg0, String arg1) throws SAXException, IOException {
				Reader i = new StringReader("<a></a>");
				return new InputSource(i);
			}
		});
		PList result = new PList();
		xmlReader.setContentHandler(result);
		xmlReader.parse(new InputSource(contents));
		return result.getResult();
	}
}
