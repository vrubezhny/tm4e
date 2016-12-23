package org.eclipse.tm4e.markdown.marked;

import java.util.ArrayList;
import java.util.Collections;

public class Tokens extends ArrayList<Token> {

	public Object links;

	public Tokens reverse() {
		Collections.reverse(this);
		return this;
	}

	public Token pop() {
		if (super.isEmpty()) {
			return null;
		}
		return super.remove(super.size() - 1);
	}

}
