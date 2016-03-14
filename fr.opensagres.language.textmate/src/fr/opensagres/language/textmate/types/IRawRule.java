package fr.opensagres.language.textmate.types;

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

	IRawRule[] getPatterns();

	void setPatterns(IRawRule[] patterns);

	IRawRepository getRepository();

	void setRepository(IRawRepository repository);

	Boolean getApplyEndPatternLast();

	void setApplyEndPatternLast(Boolean applyEndPatternLast);
}
