package org.eclipse.tm4e.markdown.marked;

public interface IRenderer {

	void code(String code, String lang, boolean escaped);

	void blockquote(String quote);

	void html(String html);

	void heading(String text, int level, String raw);

	void hr();

	void list(String body, boolean ordered);

	void listitem(String text);

	void startParagraph();
	
	void endParagraph();

	void table(String header, String body);

	void tablerow(String content);

	void tablecell(String content, String flags);

	void startEm();
	
	void endEm();
	
	void startStrong();
	
	void endStrong();
	
	void codespan(String text);

	void br();

	void del(String text);

	void link(String href, String title, String text);

	void image(String href, String title, String text);

	void text(String text);

	

}
