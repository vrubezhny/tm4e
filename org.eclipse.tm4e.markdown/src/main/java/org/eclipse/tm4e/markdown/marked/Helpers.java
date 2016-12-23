package org.eclipse.tm4e.markdown.marked;

public class Helpers {

	public static String escape(String html) {
		return escape(html, false);
	}

	public static String escape(String html, boolean encode) {
		return html
			    .replaceAll(!encode ? "&(?!#?\\w+;)" : "&", "&amp;")
			    .replaceAll("<", "&lt;")
			    .replaceAll(">", "&gt;")
			    .replaceAll("\"", "&quot;")
			    .replaceAll("'", "&#39;");
	}

	public static boolean isEmpty(String s) {
		return s == null || s.length() < 1;
	}

}
