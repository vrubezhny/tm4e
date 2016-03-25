package _editor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;

import fr.opensagres.language.textmate.grammar.IGrammar;
import fr.opensagres.language.textmate.grammar.ITokenizeLineResult;
import fr.opensagres.language.textmate.registry.Registry;

public class TextMateTokenScanner implements ITokenScanner {

	/** The token to be returned by default if no rule fires */
	protected IToken fDefaultReturnToken;
	/** The document to be scanned */
	protected IDocument fDocument;
	/** The cached legal line delimiters of the document */
	protected char[][] fDelimiters;
	/** The offset of the next character to be read */
	protected int fOffset;
	/** The end offset of the range to be scanned */
	protected int fRangeEnd;
	/** The offset of the last read token */
	protected int fTokenOffset;
	/** The cached column of the current scanner position */
	protected int fColumn;
	private IGrammar grammar;
	private ITokenizeLineResult lineTokens;
	private int i = 0;
	private int fTokenLength;

	/** Internal setting for the un-initialized column cache. */
	protected static final int UNDEFINED = -1;

	public TextMateTokenScanner() {
		Registry registry = new Registry();
		try {
			grammar = registry.loadGrammarFromPathSync("JavaScript.tmLanguage",
					Main.class.getResourceAsStream("JavaScript.tmLanguage"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int getTokenOffset() {
		return fTokenOffset;
	}

	@Override
	public int getTokenLength() {
		return fTokenLength;
		// if (fOffset < fRangeEnd)
		// return fOffset - getTokenOffset();
		// return fRangeEnd - getTokenOffset();
	}

	@Override
	public IToken nextToken() {
		fr.opensagres.language.textmate.grammar.IToken[] t = lineTokens.getTokens();
		if (t.length > i) {
			fr.opensagres.language.textmate.grammar.IToken tt = t[i];
			fTokenOffset = fOffset + tt.getStartIndex();
			int end = fOffset + tt.getEndIndex();
			if (end > fDocument.getLength()) {
				fTokenLength = (fDocument.getLength() - fOffset) - tt.getStartIndex();
			} else {
				fTokenLength = tt.getEndIndex() - tt.getStartIndex();
			}
			i++;
			return createToken(tt);
		}
		return Token.EOF;
	}

	protected IToken createToken(fr.opensagres.language.textmate.grammar.IToken tt) {
		// TODO Auto-generated method stub
		return new Token(null);
	}

	/**
	 * Configures the scanner's default return token. This is the token which is
	 * returned when none of the rules fired and EOF has not been reached.
	 *
	 * @param defaultReturnToken
	 *            the default return token
	 * @since 2.0
	 */
	public void setDefaultReturnToken(IToken defaultReturnToken) {
		Assert.isNotNull(defaultReturnToken.getData());
		fDefaultReturnToken = defaultReturnToken;
	}

	@Override
	public void setRange(final IDocument document, int offset, int length) {
		Assert.isLegal(document != null);
		final int documentLength = document.getLength();
		checkRange(offset, length, documentLength);

		fDocument = document;
		fOffset = offset;
		fColumn = UNDEFINED;
		fRangeEnd = offset + length;

		String[] delimiters = fDocument.getLegalLineDelimiters();
		fDelimiters = new char[delimiters.length][];
		for (int i = 0; i < delimiters.length; i++) {
			fDelimiters[i] = delimiters[i].toCharArray();
		}
		if (fDefaultReturnToken == null) {
			fDefaultReturnToken = new Token(null);
		}
		try {
			lineTokens = grammar.tokenizeLine(document.get(offset, length));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		i = 0;
	}

	/**
	 * Checks that the given range is valid. See
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=69292
	 *
	 * @param offset
	 *            the offset of the document range to scan
	 * @param length
	 *            the length of the document range to scan
	 * @param documentLength
	 *            the document's length
	 * @since 3.3
	 */
	private void checkRange(int offset, int length, int documentLength) {
		Assert.isLegal(offset > -1);
		Assert.isLegal(length > -1);
		Assert.isLegal(offset + length <= documentLength);
	}

}
