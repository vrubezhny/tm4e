package org.eclipse.tm4e.languageconfiguration.internal.utils;

public class TabSpacesInfo {

	private final int tabSize;
	private final boolean insertSpaces;

	public TabSpacesInfo(int tabSize, boolean insertSpaces) {
		this.tabSize = tabSize;
		this.insertSpaces = insertSpaces;
	}
	
	public int getTabSize() {
		return tabSize;
	}
	
	public boolean isInsertSpaces() {
		return insertSpaces;
	}
}
