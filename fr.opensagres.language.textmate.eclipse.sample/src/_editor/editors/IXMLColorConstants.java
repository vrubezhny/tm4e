package _editor.editors;

import org.eclipse.swt.graphics.RGB;

public interface IXMLColorConstants {
	RGB XML_COMMENT = new RGB(128, 0, 0);
	RGB PROC_INSTR = new RGB(128, 128, 128);
	RGB STRING = new RGB(0, 128, 0);
	RGB DEFAULT = new RGB(0, 0, 0);
	RGB TAG = new RGB(63, 127, 127);
	
	RGB KEY_WORD = new RGB(255, 0, 0);
	RGB KEY_COMMENTS = new RGB(255, 0, 255);
}

/**
 * <style type="text/css" class="contributedColorTheme">.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.entity.name.function { color: rgba(121, 94, 38, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.entity.method.name { color: rgba(121, 94, 38, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.meta.parameter.type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.name.class { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.storage.type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.meta.return.type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.meta.object.type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.return-type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.meta.cast { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.new.storage.type.ts { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.cast.storage.type.ts { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.heritage.storage.type.ts { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.annotation.storage.type.ts { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.var.annotation.type.ts { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.field.storage.type.ts { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.new.storage.type.js { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.cast.storage.type.js { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.heritage.storage.type.js { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.annotation.storage.type.js { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.var.annotation.type.js { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.field.storage.type.js { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.new.storage.type.tsx { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.cast.storage.type.tsx { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.heritage.storage.type.tsx { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.annotation.storage.type.tsx { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.var.annotation.type.tsx { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.field.storage.type.tsx { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.storage.type.ts { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.storage.type.js { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.storage.type.tsx { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.keyword.control { color: rgba(175, 0, 219, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.meta.preprocessor { color: rgba(175, 0, 219, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.meta.parameter.type.variable { color: rgba(0, 16, 128, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.variable.parameter { color: rgba(0, 16, 128, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.variable { color: rgba(0, 16, 128, 1); }
.monaco-editor.vs.vscode-theme-colorful-defaults-themes-light_plus-tmTheme .token.variable.name { color: rgba(0, 16, 128, 1); }</style> 
 */


/**
 .monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.entity.name.function { color: rgba(220, 220, 170, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.entity.method.name { color: rgba(220, 220, 170, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.meta.parameter.type { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.name.class { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.storage.type { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.meta.return.type { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.meta.object.type { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.return-type { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.meta.cast { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.new.storage.type.ts { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.cast.storage.type.ts { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.heritage.storage.type.ts { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.annotation.storage.type.ts { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.var.annotation.type.ts { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.field.storage.type.ts { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.new.storage.type.js { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.cast.storage.type.js { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.heritage.storage.type.js { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.annotation.storage.type.js { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.var.annotation.type.js { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.field.storage.type.js { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.new.storage.type.tsx { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.cast.storage.type.tsx { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.heritage.storage.type.tsx { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.annotation.storage.type.tsx { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.var.annotation.type.tsx { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.field.storage.type.tsx { color: rgba(78, 201, 176, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.storage.type.ts { color: rgba(86, 156, 214, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.storage.type.js { color: rgba(86, 156, 214, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.storage.type.tsx { color: rgba(86, 156, 214, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.keyword.control { color: rgba(197, 134, 192, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.meta.preprocessor { color: rgba(197, 134, 192, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.meta.parameter.type.variable { color: rgba(156, 220, 254, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.variable.parameter { color: rgba(156, 220, 254, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.variable { color: rgba(156, 220, 254, 1); }
.monaco-editor.vs-dark.vscode-theme-colorful-defaults-themes-dark_plus-tmTheme .token.variable.name { color: rgba(156, 220, 254, 1); }
**/