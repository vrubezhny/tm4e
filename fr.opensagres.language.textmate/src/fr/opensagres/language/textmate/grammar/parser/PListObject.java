package fr.opensagres.language.textmate.grammar.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.language.textmate.types.IRawRepository;
import fr.opensagres.language.textmate.types.IRawRule;

public class PListObject {

	private final PListObject parent;
	private final List<Object> arrayValues;
	private final Map<String, Object> mapValues;

	private String lastKey;

	public PListObject(PListObject parent, boolean valueAsArray) {
		this.parent = parent;
		if (valueAsArray) {
			this.arrayValues = new ArrayList<Object>();
			this.mapValues = null;
		} else {
			this.arrayValues = null;
			this.mapValues = new Raw();
		}
	}

	public PListObject getParent() {
		return parent;
	}

	public String getLastKey() {
		return lastKey;
	}

	public void setLastKey(String lastKey) {
		this.lastKey = lastKey;
	}

	public void addValue(Object value) {
		if (isValueAsArray()) {
			arrayValues.add(value);
		} else {
			mapValues.put(getLastKey(), value);
		}
	}
	// Object getValue();

	public boolean isValueAsArray() {
		return arrayValues != null;
	}

	public Object getValue() {
		if (isValueAsArray()) {
			return arrayValues;
		}
		return mapValues;
	}

	class Raw extends HashMap<String, Object> implements IRawRepository, IRawRule {

		@Override
		public IRawRule getBase() {			
			return null;
		}

		@Override
		public IRawRule getSelf() {
			return null;
		}
		
	}
}
