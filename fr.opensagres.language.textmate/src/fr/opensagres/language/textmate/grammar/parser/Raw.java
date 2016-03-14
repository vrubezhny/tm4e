package fr.opensagres.language.textmate.grammar.parser;

import java.util.HashMap;

import fr.opensagres.language.textmate.types.IRawCaptures;
import fr.opensagres.language.textmate.types.IRawGrammar;
import fr.opensagres.language.textmate.types.IRawRepository;
import fr.opensagres.language.textmate.types.IRawRule;

public class Raw extends HashMap<String, Object> implements IRawRepository, IRawRule, IRawGrammar {

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
		return (IRawRepository) super.get("repository");
	}

	@Override
	public void setRepository(IRawRepository repository) {
		super.put("repository", repository);
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

	@Override
	public String getScopeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getFileTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFirstLineMatch() {
		// TODO Auto-generated method stub
		return null;
	}

}