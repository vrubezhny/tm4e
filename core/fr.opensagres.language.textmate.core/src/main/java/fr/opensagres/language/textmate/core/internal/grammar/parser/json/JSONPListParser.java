package fr.opensagres.language.textmate.core.internal.grammar.parser.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import fr.opensagres.language.textmate.core.internal.grammar.parser.PList;
import fr.opensagres.language.textmate.core.internal.grammar.reader.IGrammarParser;
import fr.opensagres.language.textmate.core.internal.types.IRawGrammar;

public class JSONPListParser implements IGrammarParser {

	public static final IGrammarParser INSTANCE = new JSONPListParser();

	@Override
	public IRawGrammar parse(InputStream contents) throws Exception {
		PList pList = new PList();
		JsonReader reader = new JsonReader(new InputStreamReader(contents, Charset.forName("UTF-8")));
		// reader.setLenient(true);
		String lastName = null;
		boolean parsing = true;
		while (parsing) {
			JsonToken nextToken = reader.peek();
			switch (nextToken) {
			case END_DOCUMENT:
				parsing = false;
			}
			if (JsonToken.BEGIN_OBJECT.equals(nextToken)) {
				pList.startElement(null, "dict", null, null);
//				if (lastName != null) {
//					pList.startElement(null, "key", null, null);
//					pList.characters(lastName.toCharArray(), 0, lastName.length());
//					pList.endElement(null, "key", null);
//				}
				reader.beginObject();
			} else if (JsonToken.END_OBJECT.equals(nextToken)) {
				pList.endElement(null, "dict", null);
				reader.endObject();
			} else if (JsonToken.BEGIN_ARRAY.equals(nextToken)) {
//				pList.startElement(null, "key", null, null);
//				pList.characters(lastName.toCharArray(), 0, lastName.length());
//				pList.endElement(null, "key", null);
				pList.startElement(null, "array", null, null);
				//pList.startElement(null, "dict", null, null);
				reader.beginArray();
			} else if (JsonToken.END_ARRAY.equals(nextToken)) {
				//pList.endElement(null, "dict", null);
				pList.endElement(null, "array", null);
				reader.endArray();
			} else if (JsonToken.NAME.equals(nextToken)) {
				lastName = reader.nextName();
				pList.startElement(null, "key", null, null);
				pList.characters(lastName.toCharArray(), 0, lastName.length());
				pList.endElement(null, "key", null);
				// String name = reader.nextName();
				// pList.startElement(null, name, null, null);
				//System.out.println(lastName);

			} else if (JsonToken.STRING.equals(nextToken)) {
				String value = reader.nextString();
				pList.startElement(null, "string", null, null);
				pList.characters(value.toCharArray(), 0, value.length());
				pList.endElement(null, "string", null);

				//System.out.println(value);

			} else if (JsonToken.NUMBER.equals(nextToken)) {

				long value = reader.nextLong();
				System.out.println(value);

			}
		}
		// reader.endObject();
		reader.close();
		return pList.getResult();
	}

}
