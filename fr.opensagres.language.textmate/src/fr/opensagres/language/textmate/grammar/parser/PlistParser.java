package fr.opensagres.language.textmate.grammar.parser;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
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
		PList result = new PList();
		xmlReader.setContentHandler(result);
		xmlReader.parse(new InputSource(contents));
		return result;
	}
}
