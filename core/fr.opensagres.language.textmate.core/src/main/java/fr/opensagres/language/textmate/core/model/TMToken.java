package fr.opensagres.language.textmate.core.model;

public class TMToken {

	public final int startIndex;
	public final String type;

	public TMToken(int startIndex, String type) {
		this.startIndex = startIndex;
		this.type = type;
	}
}
