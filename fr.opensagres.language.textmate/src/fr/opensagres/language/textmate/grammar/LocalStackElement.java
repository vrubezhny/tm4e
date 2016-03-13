package fr.opensagres.language.textmate.grammar;

public class LocalStackElement {

	private String scopeName;
	private int endPos;

	public LocalStackElement(String scopeName, int endPos) {
		this.scopeName = scopeName;
		this.endPos = endPos;
	}

	public String getScopeName() {
		return scopeName;
	}

	public int getEndPos() {
		return endPos;
	}

}
