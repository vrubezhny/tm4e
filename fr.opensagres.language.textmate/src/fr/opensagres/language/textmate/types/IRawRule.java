package fr.opensagres.language.textmate.types;

public interface IRawRule {

	Integer getId();

	void setId(Integer id);

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

	// id?: number;
	//
	// include?: string;
	//
	// name?: string;
	// contentName?: string;
	//
	// match?:string;
	// captures?: IRawCaptures;
	// begin?:string;
	// beginCaptures?: IRawCaptures;
	// end?:string;
	// endCaptures?: IRawCaptures;
	// patterns?: IRawRule[];
	//
	// repository?: IRawRepository;
	//
	// applyEndPatternLast?:boolean;
}
