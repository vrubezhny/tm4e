package _editor.editors;

import org.eclipse.swt.graphics.RGB;

public interface IXMLColorConstants {
	RGB XML_COMMENT = new RGB(128, 0, 0);
	RGB PROC_INSTR = new RGB(128, 128, 128);
	RGB STRING = new RGB(163, 21, 21);
	RGB NUMERIC = new RGB(9, 136, 90);
	RGB DEFAULT = new RGB(0, 0, 0);
	RGB TAG = new RGB(63, 127, 127);
	
	RGB KEY_WORD = new RGB(0, 0, 255);
	RGB KEY_COMMENTS = new RGB(0, 128, 0);
	
	RGB NAME_FUNCTION = new RGB(121, 94, 38);
	RGB META_TYPE_ANNOTATION  = new RGB(38, 127, 153);
	
	RGB META_PARAMETER_TYPE_VARIABLE  = new RGB(0, 16, 128);
	
	RGB KEYWORD_CONTROL  = new RGB(175, 0, 219);
	
}

/**
 * 
 * .monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.emphasis { font-style: italic; }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.strong { font-weight: bold; }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.header { color: rgba(0, 0, 128, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.comment { color: rgba(0, 128, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.constant.language { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.constant.numeric { color: rgba(9, 136, 90, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.constant.regexp { color: rgba(129, 31, 63, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.constant.rgb-value { color: rgba(4, 81, 165, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.name.tag { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.name.selector { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.other.attribute-name { color: rgba(255, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.other.attribute-name.css { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.other.attribute-name.scss { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.invalid { color: rgba(205, 49, 49, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.underline { text-decoration: underline; }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.bold { font-weight: bold; color: rgba(0, 0, 128, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.heading { font-weight: bold; color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.italic { font-style: italic; }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.inserted { color: rgba(9, 136, 90, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.deleted { color: rgba(163, 21, 21, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.changed { color: rgba(4, 81, 165, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.punctuation.quote.beginning { color: rgba(4, 81, 165, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.punctuation.list.beginning { color: rgba(4, 81, 165, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.markup.inline.raw { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.selector { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.tag { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.preprocessor { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.preprocessor.string { color: rgba(163, 21, 21, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.preprocessor.numeric { color: rgba(9, 136, 90, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.structure.dictionary.key.python { color: rgba(4, 81, 165, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.storage { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.storage.type { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.storage.modifier { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.string { color: rgba(163, 21, 21, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.string.xml { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.string.jade { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.string.yaml { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.string.html { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.string.regexp { color: rgba(129, 31, 63, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.support.property-value { color: rgba(4, 81, 165, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.support.type.property-name.css { color: rgba(255, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.support.type.property-name.less { color: rgba(255, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.support.type.property-name.scss { color: rgba(255, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.support.type.property-name { color: rgba(4, 81, 165, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.keyword { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.keyword.control { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.keyword.operator { color: rgba(0, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.keyword.operator.new { color: rgba(0, 0, 255, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.keyword.other.unit { color: rgba(9, 136, 90, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.keyword.control.less { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.metatag.php { color: rgba(128, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.support.function.git-rebase { color: rgba(4, 81, 165, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.constant.sha.git-rebase { color: rgba(9, 136, 90, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.storage.modifier.import.java { color: rgba(0, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.storage.modifier.package.java { color: rgba(0, 0, 0, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.type.name { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.return.type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.return-type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.cast { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.type.annotation { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.support.type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.name.class { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.name.type { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.storage.type.cs { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.storage.type.java { color: rgba(38, 127, 153, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.name.function { color: rgba(121, 94, 38, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.entity.name.method { color: rgba(121, 94, 38, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.keyword.control { color: rgba(175, 0, 219, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.meta.parameter.type.variable { color: rgba(0, 16, 128, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.variable.parameter { color: rgba(0, 16, 128, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.variable { color: rgba(0, 16, 128, 1); }
.monaco-editor.vs.vscode-theme-defaults-themes-light_plus-json .token.variable.name { color: rgba(0, 16, 128, 1); }
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
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