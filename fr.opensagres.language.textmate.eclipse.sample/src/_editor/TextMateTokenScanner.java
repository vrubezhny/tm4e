package _editor;
 
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.Token;

import fr.opensagres.language.textmate.grammar.IGrammar;
import fr.opensagres.language.textmate.grammar.ITokenizeLineResult;
import fr.opensagres.language.textmate.grammar.StackElement;
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
	private static IGrammar grammar;
	private ITokenizeLineResult lineTokens;
	private int i = 0;
	private int fTokenLength;
	private List<MyToken> tokens;

	/** Internal setting for the un-initialized column cache. */
	protected static final int UNDEFINED = -1;

	public TextMateTokenScanner() {
		Registry registry = new Registry();
		try {
			if (grammar == null) {
				long start = System.currentTimeMillis();
				grammar = registry.loadGrammarFromPathSync("Angular2TypeScript.tmLanguage",
						Main.class.getResourceAsStream("Angular2TypeScript.tmLanguage"));
				System.err.println("Grammar loaded with " + (System.currentTimeMillis() - start) + "ms");
			}
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

	private IToken lastToken;

	@Override
	public IToken nextToken() {
		if (tokens.size() > i) {
			MyToken t = tokens.get(i);
			IToken token = createToken(t.getT());
			if (token.equals(lastToken)) {
				fTokenLength = t.getLength()
						+ (t.getOffset() - tokens.get(i - 1).getLength() - tokens.get(i - 1).getOffset());
				fTokenOffset = t.getOffset();
			} else {
				fTokenOffset = t.getOffset();
				fTokenLength = t.getLength();
			}
			lastToken = token;
			i++;
			return token;

		}

		// fr.opensagres.language.textmate.grammar.IToken[] t =
		// tokens.toArray(new
		// fr.opensagres.language.textmate.grammar.IToken[0]);//lineTokens.getTokens();
		// if (t.length > i) {
		// fr.opensagres.language.textmate.grammar.IToken tt = t[i];
		// fTokenOffset = fOffset + tt.getStartIndex();
		// int end = fOffset + tt.getEndIndex();
		// if (end > fDocument.getLength()) {
		// fTokenLength = (fDocument.getLength() - fOffset) -
		// tt.getStartIndex();
		// } else {
		// fTokenLength = tt.getEndIndex() - tt.getStartIndex();
		// }
		// i++;
		// return createToken(tt);
		// }
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

	private class MyToken {

		private final fr.opensagres.language.textmate.grammar.IToken t;
		private final int offset;
		private final int tokenLength;

		public MyToken(fr.opensagres.language.textmate.grammar.IToken t, int startOffset) {
			this.t = t;
			this.offset = startOffset + t.getStartIndex();
			int end = startOffset + t.getEndIndex();
			if (end > fDocument.getLength()) {
				tokenLength = (fDocument.getLength() - startOffset) - t.getStartIndex();
			} else {
				tokenLength = t.getEndIndex() - t.getStartIndex();
			}
			// System.err.println(tokenLength);
		}

		public int getLength() {
			// TODO Auto-generated method stub
			return tokenLength;
		}

		public int getOffset() {
			// TODO Auto-generated method stub
			return offset;
		}

		public fr.opensagres.language.textmate.grammar.IToken getT() {
			return t;
		}
	}

	private TMModel tmModel;

	@Override
	public void setRange(final IDocument document, int offset, int length) {
		Assert.isLegal(document != null);
		final int documentLength = document.getLength();
		checkRange(offset, length, documentLength);

		fDocument = document;
		if (tmModel == null) {
			tmModel = new TMModel(document);
			//fDocument.addDocumentListener(tmModel);
		}

		fOffset = offset;
		fColumn = UNDEFINED;
		fRangeEnd = offset + length;
		lastToken = null;
		String[] delimiters = fDocument.getLegalLineDelimiters();
		fDelimiters = new char[delimiters.length][];
		for (int i = 0; i < delimiters.length; i++) {
			fDelimiters[i] = delimiters[i].toCharArray();
		}
		if (fDefaultReturnToken == null) {
			fDefaultReturnToken = new Token(null);
		}

		try {
			long start = System.currentTimeMillis();
			// lineTokens = grammar.tokenizeLine(content);
			// System.err.println(System.currentTimeMillis() - start);
			if (tokens != null) {

			}
			tokens = new ArrayList<MyToken>();

			int startLine = document.getLineOfOffset(offset);
			int l = document.getNumberOfLines(offset, length);

			start = System.currentTimeMillis();
			List<StackElement> prevState = startLine > 0 ? tmModel.getLineContext(startLine - 1) : null;
			int lastLineDelim = 0;
			for (int i = startLine; i < startLine + l; i++) {
				int lo = document.getLineOffset(i);
				String delim = document.getLineDelimiter(i);
				int ll = document.getLineLength(i) - (delim != null ? delim.length() : 0);
				String lc = document.get(lo, ll);
				// System.err.println(lc);
				
				ITokenizeLineResult tr = grammar.tokenizeLine(lc, prevState);
				prevState = tr.getRuleStack();
				tmModel.setLineContext(i, new ArrayList(prevState));
				
				fr.opensagres.language.textmate.grammar.IToken[] t = tr.getTokens();
				for (int j = 0; j < t.length; j++) {
					tokens.add(new MyToken(t[j], lo + lastLineDelim));
				}
			}
			System.err.println(System.currentTimeMillis() - start + "ms");

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
