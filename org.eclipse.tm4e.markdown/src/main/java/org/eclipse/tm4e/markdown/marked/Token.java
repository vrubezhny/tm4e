package org.eclipse.tm4e.markdown.marked;

public class Token {

	public final TokenType type;
	public final String text;
	public final int depth;
	public final String lang;
	public final boolean escaped;

	public Token(TokenType type) {
		this(type, null);
	}

	public Token(TokenType type, String text) {
		this(type, text, -1);
	}

	public Token(TokenType type, String text, int depth) {
		this(type, text, depth, null, false);
	}

	public Token(TokenType type, String lang, String text) {
		this(type, text, -1, lang, false);
	}

	private Token(TokenType type, String text, int depth, String lang, boolean escaped) {
		this.type = type;
		this.text = text;
		this.depth = depth;
		this.lang = lang;
		this.escaped = escaped;
	}
}
