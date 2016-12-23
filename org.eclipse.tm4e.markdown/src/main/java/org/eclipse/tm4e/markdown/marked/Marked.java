package org.eclipse.tm4e.markdown.marked;

public class Marked {

	public static IRenderer parse(String src) {
		return parse(src, (Options) null);
	}

	public static IRenderer parse(String src, IRenderer renderer) {
		return parse(src, null, renderer);
	}

	public static IRenderer parse(String src, Options opt) {
		return parse(src, opt, null);
	}

	public static IRenderer parse(String src, Options opt, IRenderer renderer) {

		return Parser.parse(Lexer.lex(src, opt), opt, renderer);
	}
}
