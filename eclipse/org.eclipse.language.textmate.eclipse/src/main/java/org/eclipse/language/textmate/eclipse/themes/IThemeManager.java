package org.eclipse.language.textmate.eclipse.themes;

public interface IThemeManager {

	ITokenProvider getThemeFor(String contentTypeId);
	
	ITokenProvider getThemeById(String themeId);
}
