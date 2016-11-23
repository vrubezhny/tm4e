package org.eclipse.textmate4e.core.internal.types;

import java.util.Collection;

public interface IRawRule {

	Integer getId();

	void setId(Integer id);

	String getInclude();

	void setInclude(String include);

	String getName();

	void setName(String name);

	String getContentName();

	void setContentName(String name);

	String getMatch();

	void setMatch(String match);

	IRawCaptures getCaptures();

	void setCaptures(IRawCaptures captures);

	String getBegin();

	void setBegin(String begin);

	IRawCaptures getBeginCaptures();

	void setBeginCaptures(IRawCaptures beginCaptures);

	String getEnd();

	void setEnd(String end);

	IRawCaptures getEndCaptures();

	void setEndCaptures(IRawCaptures endCaptures);

	Collection<IRawRule> getPatterns();

	void setPatterns(Collection<IRawRule> patterns);

	IRawRepository getRepository();

	void setRepository(IRawRepository repository);

	boolean isApplyEndPatternLast();

	void setApplyEndPatternLast(boolean applyEndPatternLast);
}
