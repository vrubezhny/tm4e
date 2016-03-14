package fr.opensagres.language.textmate.grammar.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.opensagres.language.textmate.types.IRawCaptures;
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

		@Override
		public Integer getId() {
			return (Integer) super.get("id");
		}

		@Override
		public void setId(Integer id) {
			super.put("id", id);
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setName(String name) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getContentName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setContentName(String name) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getMatch() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setMatch(String match) {
			// TODO Auto-generated method stub

		}

		@Override
		public IRawCaptures getCaptures() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setCaptures(IRawCaptures captures) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getBegin() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setBegin(String begin) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getInclude() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setInclude(String include) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public IRawCaptures getBeginCaptures() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setBeginCaptures(IRawCaptures beginCaptures) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getEnd() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setEnd(String end) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public IRawCaptures getEndCaptures() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setEndCaptures(IRawCaptures endCaptures) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public IRawRule[] getPatterns() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setPatterns(IRawRule[] patterns) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public IRawRepository getRepository() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setRepository(IRawRepository repository) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Boolean getApplyEndPatternLast() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setApplyEndPatternLast(Boolean applyEndPatternLast) {
			// TODO Auto-generated method stub
			
		}

	}
}
