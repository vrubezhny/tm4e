package org.eclipse.tm4e.languageconfiguration;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.tm4e.languageconfiguration.internal.IAutoClosingPair;
import org.eclipse.tm4e.languageconfiguration.internal.LanguageConfigurationRegistry;
import org.eclipse.tm4e.ui.internal.model.ContentTypeHelper;
import org.eclipse.tm4e.ui.internal.model.ContentTypeHelper.ContentTypeInfo;

public class LanguageConfigurationAutoEditStrategy implements IAutoEditStrategy {

	private IDocument document;
	private IContentType[] contentTypes;

	@Override
	public void customizeDocumentCommand(IDocument document, DocumentCommand command) {
		IContentType[] contentTypes = findContentTypes(document);
		if (contentTypes == null) {
			return;
		}

		// Auto close pair
		LanguageConfigurationRegistry registry = LanguageConfigurationRegistry.getInstance();
		for (IContentType contentType : contentTypes) {
			List<IAutoClosingPair> autoClosingPairs = registry.getAutoClosingPairs(contentType.getId());
			if (autoClosingPairs != null) {
				for (IAutoClosingPair autoClosingPair : autoClosingPairs) {
					if (command.text.equals(autoClosingPair.getOpen())) {
						command.text += autoClosingPair.getClose();
						command.caretOffset = command.offset + 1;
						command.shiftsCaret = false;
						break;
					}
				}
			}
		}

		// ITMModel model = TMUIPlugin.getTMModelManager().connect(document);
		// try {
		// int lineNumber = document.getLineOfOffset(command.offset);
		// model.forceTokenization(lineNumber);
		// List<TMToken> tokens = model.getLineTokens(lineNumber);
		// System.err.println(tokens.size());
		// } catch (BadLocationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

//		if (registry.shouldAutoClosePair(command.text, ".java")) {
//			List<IAutoClosingPair> autoClosingPairs = registry.getAutoClosingPairs(".java");
//			for (IAutoClosingPair autoClosingPair : autoClosingPairs) {
//				if (command.text.equals(autoClosingPair.getOpen())) {
//					command.text += autoClosingPair.getClose();
//					command.caretOffset = command.offset + 1;
//					command.shiftsCaret = false;
//					break;
//				}
//			}
//		}
	}

	private IContentType[] findContentTypes(IDocument document) {
		if (this.document != null && this.document.equals(document)) {
			return contentTypes;
		}
		try {
			ContentTypeInfo info = ContentTypeHelper.findContentTypes(document);
			this.contentTypes = info.getContentTypes();
			this.document = document;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return contentTypes;
	}

}
