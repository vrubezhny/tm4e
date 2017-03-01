# Eclipse tm4e - TextMate support in Eclipse IDE

[![Eclipse License](http://img.shields.io/badge/license-Eclipse-brightgreen.svg)](https://github.com/eclipse/tm4e/blob/master/LICENSE)
[![Build Status](https://secure.travis-ci.org/eclipse/tm4e.png)](http://travis-ci.org/eclipse/tm4e)

`tm4e` provides the capability to support TextMate tokenizer with Java. It provides:

 * [org.eclipse.tm4e.core](https://github.com/eclipse/tm4e/tree/master/org.eclipse.tm4e.core) provides the Java TextMate tokenizer. This project is a Java port of [vscode-textmate](https://github.com/Microsoft/vscode-textmate) written in TypeScript. This Java API can be used with any Java UI Toolkit (Swing, Eclipse, etc). See [Core](https://github.com/eclipse/tm4e/wiki/Core) section for more information.

 * [org.eclipse.tm4e.ui](https://github.com/eclipse/tm4e/tree/master/org.eclipse.tm4e.ui) provides the Eclipse **org.eclipse.jface.text.presentation.IPresentationReconciler** [TMPresentationReconciler](https://github.com/eclipse/tm4e/blob/master/org.eclipse.tm4e.ui/src/main/java/org/eclipse/tm4e/ui/text/TMPresentationReconciler.java) which is able to tokenize an editor content by using a given JSON, PList TextMate grammar and do syntax coloration. See [UI](https://github.com/eclipse/tm4e/wiki/UI) section for more information.

Here a sample with TypeScript:

![TypeScript Editor](https://github.com/eclipse/tm4e/wiki/images/TypeScriptEditor.png)

### Update Site

You can install `tm4e` with the update site TODO which provides samples of syntax coloration with:

 * GenericEditor for Language Server (C#, CSS, JSON)
 * a TypeScript Editor.
 
### Feedback

* **Support:** You can ask questions, report bugs, and request features using [GitHub issues](http://github.com/eclipse/tm4e/issues).

### License

`tm4e` is open sourced under the Eclipse Public License 1.0.