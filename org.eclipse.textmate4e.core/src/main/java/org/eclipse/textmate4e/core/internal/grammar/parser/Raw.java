package org.eclipse.textmate4e.core.internal.grammar.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.textmate4e.core.internal.types.IRawCaptures;
import org.eclipse.textmate4e.core.internal.types.IRawGrammar;
import org.eclipse.textmate4e.core.internal.types.IRawRepository;
import org.eclipse.textmate4e.core.internal.types.IRawRule;

public class Raw extends HashMap<String, Object> implements IRawRepository, IRawRule, IRawGrammar, IRawCaptures {

	@Override
	public IRawRule getProp(String name) {
		return (IRawRule) super.get(name);
	}

	@Override
	public IRawRule getBase() {
		return (IRawRule) super.get("$base");
	}

	@Override
	public void setBase(IRawRule base) {
		super.put("$base", base);
	}

	@Override
	public IRawRule getSelf() {
		return (IRawRule) super.get("$self");
	}

	@Override
	public void setSelf(IRawRule self) {
		super.put("$self", self);
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
		return (String) super.get("name");
	}

	@Override
	public void setName(String name) {
		super.put("name", name);
	}

	@Override
	public String getContentName() {
		return (String) super.get("contentName");
	}

	@Override
	public void setContentName(String name) {
		super.put("contentName", name);
	}

	@Override
	public String getMatch() {
		return (String) super.get("match");
	}

	@Override
	public void setMatch(String match) {
		super.put("match", match);
	}

	@Override
	public IRawCaptures getCaptures() {
		return (IRawCaptures) super.get("captures");
	}

	@Override
	public void setCaptures(IRawCaptures captures) {
		super.put("captures", captures);
	}

	@Override
	public String getBegin() {
		return (String) super.get("begin");
	}

	@Override
	public void setBegin(String begin) {
		super.put("begin", begin);
	}

	@Override
	public String getWhile() {
		return (String) super.get("while");
	}

	@Override
	public String getInclude() {
		return (String) super.get("include");
	}

	@Override
	public void setInclude(String include) {
		super.put("include", include);
	}

	@Override
	public IRawCaptures getBeginCaptures() {
		return (IRawCaptures) super.get("beginCaptures");
	}

	@Override
	public void setBeginCaptures(IRawCaptures beginCaptures) {
		super.put("beginCaptures", beginCaptures);
	}

	@Override
	public String getEnd() {
		return (String) super.get("end");
	}

	@Override
	public void setEnd(String end) {
		super.put("end", end);
	}

	@Override
	public IRawCaptures getEndCaptures() {
		return (IRawCaptures) super.get("endCaptures");
	}

	@Override
	public void setEndCaptures(IRawCaptures endCaptures) {
		super.put("endCaptures", endCaptures);
	}

	@Override
	public IRawCaptures getWhileCaptures() {
		return (IRawCaptures) super.get("whileCaptures");
	}

	@Override
	public Collection<IRawRule> getPatterns() {
		return (Collection<IRawRule>) super.get("patterns");
	}

	@Override
	public void setPatterns(Collection<IRawRule> patterns) {
		super.put("patterns", patterns);
	}

	@Override
	public Map<String, IRawRule> getInjections() {
		return (Map<String, IRawRule>) super.get("injections");
	}

	@Override
	public String getInjectionSelector() {
		return (String) super.get("injectionSelector");
	}

	@Override
	public IRawRepository getRepository() {
		return (IRawRepository) super.get("repository");
	}

	@Override
	public void setRepository(IRawRepository repository) {
		super.put("repository", repository);
	}

	@Override
	public boolean isApplyEndPatternLast() {
		Object applyEndPatternLast = super.get("applyEndPatternLast");
		if (applyEndPatternLast == null) {
			return false;
		}
		if (applyEndPatternLast instanceof Boolean) {
			return (Boolean) applyEndPatternLast;
		}
		if (applyEndPatternLast instanceof Integer) {
			return ((Integer) applyEndPatternLast).equals(1);
		}
		return false;
	}

	@Override
	public void setApplyEndPatternLast(boolean applyEndPatternLast) {
		super.put("applyEndPatternLast", applyEndPatternLast);
	}

	@Override
	public String getScopeName() {
		return (String) super.get("scopeName");
	}

	@Override
	public Collection<String> getFileTypes() {
		return (Collection<String>) super.get("fileTypes");
	}

	@Override
	public String getFirstLineMatch() {
		return (String) super.get("firstLineMatch");
	}

	@Override
	public IRawRule getCapture(String captureId) {
		return getProp(captureId);
	}

	@Override
	public Iterator<String> iterator() {
		return super.keySet().iterator();
	}

	@Override
	public Object clone() {
		return clone(this);
	}

	private Object clone(Object value) {
		if (value instanceof Raw) {
			Raw rowToClone = (Raw) value;
			Raw raw = new Raw();
			for (Entry<String, Object> entry : rowToClone.entrySet()) {
				raw.put(entry.getKey(), clone(entry.getValue()));
			}
			return raw;
		} else if (value instanceof List) {
			List listToClone = (List) value;
			List list = new ArrayList<>();
			for (Object item : listToClone) {
				list.add(clone(item));
			}
			return list;
		} else if (value instanceof String) {
			return new String((String) value);
		} else if (value instanceof Integer) {
			return value;
		} else if (value instanceof Boolean) {
			return value;
		}
		return value;
	}

}