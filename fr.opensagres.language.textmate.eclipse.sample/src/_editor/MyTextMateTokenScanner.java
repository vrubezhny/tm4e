package _editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import _editor.editors.ColorManager;
import _editor.editors.IXMLColorConstants;

public class MyTextMateTokenScanner extends TextMateTokenScanner {

	private ColorManager manager;
	private Token docToken;
	private Token wordToken;

	public MyTextMateTokenScanner(ColorManager colorManager) {
		this.manager = colorManager;
		docToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.KEY_COMMENTS)));
		wordToken = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.KEY_WORD)));
	}

	@Override
	protected IToken createToken(fr.opensagres.language.textmate.grammar.IToken tt) {
		// System.err.println(tt.getScopes());
		if (tt.getScopes().contains("comment.block.documentation.ts"))  {
			return docToken;
		}
		if (tt.getScopes().contains("comment.block.ts"))  {
			return docToken;
		}
		
		if (tt.getScopes().contains("storage.type.function.js") || tt.getScopes().contains("storage.type.function.ts")
				|| tt.getScopes().contains("storage.type.js")) {
			return wordToken;
		} else if (tt.getScopes().contains("comment.block.documentation")) {
			return new Token(new TextAttribute(manager.getColor(IXMLColorConstants.STRING)));
		} else if (tt.getScopes().contains("entity.name.tag.js") || tt.getScopes().contains("entity.name.tag.ts")) {
			return new Token(new TextAttribute(manager.getColor(IXMLColorConstants.TAG)));
		}
		// System.err.println(tt.getScopes());
		return new Token(new TextAttribute(manager.getColor(IXMLColorConstants.DEFAULT)));
	}

}
