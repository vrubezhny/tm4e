package _editor.editors.tm;

public class Token {

	public final int startIndex;
	public final String type;

	public Token(int startIndex, String type) {
		this.startIndex = startIndex;
		this.type = type;
	}
}
