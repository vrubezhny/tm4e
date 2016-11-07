package _editor.editors.tm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

import _editor.Main;
import fr.opensagres.language.textmate.core.grammar.IGrammar;
import fr.opensagres.language.textmate.core.grammar.IToken;
import fr.opensagres.language.textmate.core.grammar.ITokenizeLineResult;
import fr.opensagres.language.textmate.core.registry.Registry;

public class TMModel2 {

	private final List<ModelLine> _lines;

	private int _invalidLineStartIndex;
	private TMState _lastState;

	private final IDocument document;
	private DecodeMap _decodeMap;

	private final List<IModelTokensChangedListener> listeners;
	/** Queue to manage the changes applied to the text viewer. */
	private DirtyRegionQueue fDirtyRegionQueue;

	enum Changed {
		Insert, Delete, Update;
	}

	private class InternalListener implements IDocumentListener {

		private final List<Changed> changes = new ArrayList<>();

		private class LineChanged {
			public final int line;
			public final Changed changed;

			public LineChanged(int line, Changed changed) {
				this.line = line;
				this.changed = changed;
			}
		}

		@Override
		public void documentAboutToBeChanged(DocumentEvent e) {
			try {
				if (e.getLength() == 0 && e.getText() != null) {
					// Insert
					//System.err.println("Insert");
				} else if (e.getText() == null || e.getText().length() == 0) {
					// Remove
					//System.err.println("Remove");
					removeLine(e);
				} else {
					// Replace (Remove + Insert)
					removeLine(e);
					//System.err.println("Insert+Remove");
				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// fireTextChanging();
		}

		private void removeLine(DocumentEvent e) throws BadLocationException {
			int startLine = document.getLineOfOffset(e.getOffset());
			int endLine = document.getLineOfOffset(e.getOffset() + e.getLength());
			int nbLignes = endLine - startLine;
			for (int i = endLine; i > startLine; i--) {
				_lines.remove(i);
			}
		}

		@Override
		public void documentChanged(DocumentEvent event) {
			documentChangedOLD(event);
		}

		// @Override
		public void documentChangedOLD(DocumentEvent event) {
			createDirtyRegion(event);
			DirtyRegion r = null;
			synchronized (fDirtyRegionQueue) {
				r = fDirtyRegionQueue.removeNextDirtyRegion();
			}

			// fIsActive= true;

			// fProgressMonitor.setCanceled(false);

			// r.getType().eq
			IDocument document = event.getDocument();
			try {
				int startLine = document.getLineOfOffset(r.getOffset());
				if (_lines.size() != document.getNumberOfLines()) {
					// Update lines
					if (r.getType().equals(DirtyRegion.INSERT)) {
						// Add new lines
						int endLine = document.getLineOfOffset(r.getOffset() + r.getLength());
						int nbLignes = endLine - startLine;
						for (int i = 0; i < nbLignes; i++) {
							int line = i + startLine + 1;
							_lines.add(line, new ModelLine(getLineText(line)));
						}
						//startLine--;
						// for (int line = startLine; line < (endLine -
						// startLine); line++) {
						// _lines.add(i + startLine, new
						// ModelLine(getLineText(i)));
						// }

					} else {
						// remove lines
//						int endLine = document.getLineOfOffset(r.getOffset() + r.getLength());
//						int nbLignes = endLine - startLine;
//						for (int i = 0; i < nbLignes; i++) {
//							int line = i + startLine;
//							_lines.remove(line);
//						}
					}
				} else {
					_lines.get(startLine).text = document.get(document.getLineOffset(startLine), document.getLineLength(startLine));
				}
				_invalidateLine(startLine);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Creates a dirty region for a document event and adds it to the queue.
	 *
	 * @param e
	 *            the document event for which to create a dirty region
	 */
	private void createDirtyRegion(DocumentEvent e) {
		synchronized (fDirtyRegionQueue) {
			if (e.getLength() == 0 && e.getText() != null) {
				// Insert
				fDirtyRegionQueue.addDirtyRegion(
						new DirtyRegion(e.getOffset(), e.getText().length(), DirtyRegion.INSERT, e.getText()));

			} else if (e.getText() == null || e.getText().length() == 0) {
				// Remove
				fDirtyRegionQueue
						.addDirtyRegion(new DirtyRegion(e.getOffset(), e.getLength(), DirtyRegion.REMOVE, null));

			} else {
				// Replace (Remove + Insert)
				fDirtyRegionQueue
						.addDirtyRegion(new DirtyRegion(e.getOffset(), e.getLength(), DirtyRegion.REMOVE, null));
				fDirtyRegionQueue.addDirtyRegion(
						new DirtyRegion(e.getOffset(), e.getText().length(), DirtyRegion.INSERT, e.getText()));
			}
		}
	}

	private InternalListener listener = new InternalListener();

	private BackgroundThread fThread;

	private class ModelLine {

		private boolean isInvalid;
		private TMState state;
		private List<Token> tokens;
		private String text;

		public ModelLine(String text) {
			this.text = text;
		}

		public void resetTokenizationState() {
			this.state = null;
			this.tokens = null;
		}

		public TMState getState() {
			return state;
		}

		public void setState(TMState state) {
			this.state = state;
		}

		public void setTokens(List<Token> tokens) {
			this.tokens = tokens;
		}

		public List<Token> getTokens() {
			return tokens;
		}
	}

	private class LineTokens implements ILineTokens {

		private List<Token> tokens;
		private int actualStopOffset;
		private TMState endState;

		public LineTokens(List<Token> tokens, int actualStopOffset, TMState endState) {
			this.tokens = tokens;
			this.actualStopOffset = actualStopOffset;
			this.endState = endState;
		}

		public TMState getEndState() {
			return endState;
		}

		public void setEndState(TMState endState) {
			this.endState = endState;
		}

	}

	private static IGrammar grammar;
	static {
		try {
			Registry registry = new Registry();
			grammar = registry.loadGrammarFromPathSync("Angular2TypeScript.tmLanguage",
					Main.class.getResourceAsStream("Angular2TypeScript.tmLanguage"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TMModel2(IDocument document, boolean lazyLoad) {
		this.document = document;
		this.document.addDocumentListener(listener);
		this._decodeMap = new DecodeMap();
		this.listeners = new ArrayList<>();
		_lines = new ArrayList<>();
		if (lazyLoad) {
			load();
		}
		fDirtyRegionQueue = new DirtyRegionQueue();
		new BackgroundThread(getClass().getName());
	}

	public void load() {
		int linesLength = document.getNumberOfLines();
		for (int line = 0; line < linesLength; line++) {
			try {
				_lines.add(new ModelLine(getLineText(line)));
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_resetTokenizationState();
	}

	public void flushLineEdits() {

	}

	protected void _invalidateLine(int lineIndex) {
		print();
		this._lines.get(lineIndex).isInvalid = true;
		if (lineIndex < this._invalidLineStartIndex) {
			if (this._invalidLineStartIndex < this._lines.size()) {
				this._lines.get(this._invalidLineStartIndex).isInvalid = true;
			}
			this._invalidLineStartIndex = lineIndex;
			this._beginBackgroundTokenization();
		}
	}

	private void print() {
		for (ModelLine modelLine : _lines) {
			//System.err.print(modelLine.text);
		}
		
	}

	private void _resetTokenizationState() {
		for (ModelLine line : this._lines) {
			line.resetTokenizationState();
		}
		_lines.get(0).setState(new TMState(null));
		this._invalidLineStartIndex = 0;
		this._beginBackgroundTokenization();
	}

	/**
	 * Background thread for the reconciling activity.
	 */
	class BackgroundThread extends Thread {

		/**
		 * Creates a new background thread. The thread runs with minimal
		 * priority.
		 *
		 * @param name
		 *            the thread's name
		 */
		public BackgroundThread(String name) {
			super(name);
			setPriority(Thread.MIN_PRIORITY);
			setDaemon(true);
		}
	}

	private void _beginBackgroundTokenization() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				_revalidateTokensNow(null);
			}
		}).start();
	}

	private void _revalidateTokensNow(Integer toLineNumber) {
		if (toLineNumber == null) {
			toLineNumber = this._invalidLineStartIndex + 1000000;
		}
		toLineNumber = Math.min(this._lines.size(), toLineNumber);

		int fromLineNumber = this._invalidLineStartIndex + 1;

		// sw = StopWatch.create(false),
		long MAX_ALLOWED_TIME = 20, tokenizedChars = 0, currentCharsToTokenize = 0, currentEstimatedTimeToTokenize = 0,
				elapsedTime;
		long startTime = System.currentTimeMillis();
		// Tokenize at most 1000 lines. Estimate the tokenization speed per
		// character and stop when:
		// - MAX_ALLOWED_TIME is reachedt
		// - tokenizing the next line would go above MAX_ALLOWED_TIME

		for (int lineNumber = fromLineNumber; lineNumber <= toLineNumber; lineNumber++) {
			elapsedTime = System.currentTimeMillis() - startTime;// sw.elapsed();
			if (elapsedTime > MAX_ALLOWED_TIME) {
				// Stop if MAX_ALLOWED_TIME is reached
				toLineNumber = lineNumber - 1;
				break;
			}

			// Compute how many characters will be tokenized for this line
			try {
				currentCharsToTokenize = document.getLineLength(lineNumber - 1);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // this._lines.get(lineNumber - 1).getLength();

			if (tokenizedChars > 0) {
				// If we have enough history, estimate how long tokenizing this
				// line would take
				currentEstimatedTimeToTokenize = (elapsedTime / tokenizedChars) * currentCharsToTokenize;
				if (elapsedTime + currentEstimatedTimeToTokenize > MAX_ALLOWED_TIME) {
					// Tokenizing this line will go above MAX_ALLOWED_TIME
					toLineNumber = lineNumber - 1;
					break;
				}
			}

			this._updateTokensUntilLine(lineNumber, false);
			tokenizedChars += currentCharsToTokenize;
		}

		elapsedTime = System.currentTimeMillis() - startTime;// sw.elapsed();

		if (fromLineNumber <= toLineNumber) {
			this.emitModelTokensChangedEvent(fromLineNumber, toLineNumber);
		}

		if (this._invalidLineStartIndex < this._lines.size()) {
			this._beginBackgroundTokenization();
		}

	}

	private void emitModelTokensChangedEvent(int fromLineNumber, Integer toLineNumber) {
		for (IModelTokensChangedListener listener : listeners) {
			//listener.modelTokensChanged(fromLineNumber - 1, toLineNumber - 1, this);
		}
	}

	private void _updateTokensUntilLine(int lineNumber, boolean emitEvents) {
		int linesLength = this._lines.size();
		int endLineIndex = lineNumber - 1;
		int stopLineTokenizationAfter = 1000000000; // 1 billion, if a line is
													// so long, you have other
													// trouble :).

		int fromLineNumber = this._invalidLineStartIndex + 1, toLineNumber = lineNumber;

		// Validate all states up to and including endLineIndex
		for (int lineIndex = this._invalidLineStartIndex; lineIndex <= endLineIndex; lineIndex++) {
			int endStateIndex = lineIndex + 1;
			LineTokens r = null;
			String text = null;
			ModelLine modeLine = this._lines.get(lineIndex);
			try {
				text = getLineText(lineIndex);
				// Tokenize only the first X characters
				r = tokenize(text, modeLine.getState(), 0, stopLineTokenizationAfter);
			} catch (Throwable e) {
				// e.friendlyMessage =
				// TextModelWithTokens.MODE_TOKENIZATION_FAILED_MSG;
				// onUnexpectedError(e);
				e.printStackTrace();
			}

			if (r != null && r.tokens != null && r.tokens.size() > 0) {
				// Cannot have a stop offset before the last token
				r.actualStopOffset = Math.max(r.actualStopOffset, r.tokens.get(r.tokens.size() - 1).startIndex + 1);
			}

			if (r != null && r.actualStopOffset < text.length()) {
				// Treat the rest of the line (if above limit) as one default
				// token
				r.tokens.add(new Token(r.actualStopOffset, ""));

				// Use as end state the starting state
				r.endState = modeLine.getState();
			}

			if (r == null) {
				// TODO
				r = nullTokenize(text, modeLine.getState());
			}
			// if (!r.modeTransitions) {
			// r.modeTransitions = [];
			// }
			// if (r.modeTransitions.length === 0) {
			// // Make sure there is at least the transition to the top-most
			// mode
			// r.modeTransitions.push(new ModeTransition(0, this.getModeId()));
			// }
			// modeLine.setTokens(this._tokensInflatorMap, r.tokens,
			// r.modeTransitions);
			modeLine.setTokens(r.tokens);
			modeLine.isInvalid = false;

			if (endStateIndex < linesLength) {
				ModelLine endStateLine = this._lines.get(endStateIndex);
				if (endStateLine.getState() != null && r.endState.equals(endStateLine.getState())) {
					// The end state of this line remains the same
					int nextInvalidLineIndex = lineIndex + 1;
					while (nextInvalidLineIndex < linesLength) {
						if (this._lines.get(nextInvalidLineIndex).isInvalid) {
							break;
						}
						if (nextInvalidLineIndex + 1 < linesLength) {
							if (this._lines.get(nextInvalidLineIndex + 1).getState() == null) {
								break;
							}
						} else {
							if (this._lastState == null) {
								break;
							}
						}
						nextInvalidLineIndex++;
					}
					this._invalidLineStartIndex = Math.max(this._invalidLineStartIndex, nextInvalidLineIndex);
					lineIndex = nextInvalidLineIndex - 1; // -1 because the
															// outer loop
															// increments it
				} else {
					endStateLine.setState(r.endState);
				}
			} else {
				this._lastState = r.endState;
			}
		}
		this._invalidLineStartIndex = Math.max(this._invalidLineStartIndex, endLineIndex + 1);

		if (emitEvents && fromLineNumber <= toLineNumber) {
			this.emitModelTokensChangedEvent(fromLineNumber, toLineNumber);
		}

	}

	private LineTokens nullTokenize(String buffer, TMState state) {
		int deltaOffset = 0;
		List<Token> tokens = new ArrayList<>();
		tokens.add(new Token(deltaOffset, ""));

		return new LineTokens(tokens, deltaOffset + buffer.length(), state);
	}

	private String getLineText(int line) throws BadLocationException {
		int lo = document.getLineOffset(line);
		String delim = document.getLineDelimiter(line);
		int ll = document.getLineLength(line) - (delim != null ? delim.length() : 0);
		return document.get(lo, ll);
	}

	private String getLineText2(int line) throws BadLocationException {
		int lo = document.getLineOffset(line);
		String delim = document.getLineDelimiter(line);
		int ll = document.getLineLength(line) - (delim != null ? delim.length() : 0);
		return document.get(lo, ll);
	}
	
	// TMSyntax

	private LineTokens tokenize(String line, TMState state, int offsetDelta, int stopLineTokenizationAfter) {
		// Do not attempt to tokenize if a line has over 20k
		// or if the rule stack contains more than 100 rules (indicator of
		// broken grammar that forgets to pop rules)
		// if (line.length >= 20000 || depth(state.getRuleStack()) > 100) {
		// return new LineTokens(
		// [new Token(offsetDelta, '')],
		// [new ModeTransition(offsetDelta, state.getModeId())],
		// offsetDelta,
		// state
		// );
		// }
		TMState freshState = state.clone();
		ITokenizeLineResult textMateResult = grammar.tokenizeLine(line, freshState.getRuleStack());
		freshState.setRuleStack(textMateResult.getRuleStack());

		// Create the result early and fill in the tokens later
		List<Token> tokens = new ArrayList<>();
		String lastTokenType = null;
		for (int tokenIndex = 0, len = textMateResult.getTokens().length; tokenIndex < len; tokenIndex++) {
			IToken token = textMateResult.getTokens()[tokenIndex];
			int tokenStartIndex = token.getStartIndex();
			String tokenType = decodeTextMateToken(this._decodeMap, token.getScopes().toArray(new String[0]));

			// do not push a new token if the type is exactly the same (also
			// helps with ligatures)
			if (!tokenType.equals(lastTokenType)) {
				tokens.add(new Token(tokenStartIndex + offsetDelta, tokenType));
				lastTokenType = tokenType;
			}
		}
		return new LineTokens(tokens, offsetDelta + line.length(), freshState);
	}

	private String decodeTextMateToken(DecodeMap decodeMap, String[] scopes) {
		String[] prevTokenScopes = decodeMap.prevToken.scopes;
		int prevTokenScopesLength = prevTokenScopes.length;
		Map<Integer, Map<Integer, Boolean>> prevTokenScopeTokensMaps = decodeMap.prevToken.scopeTokensMaps;

		Map<Integer, Map<Integer, Boolean>> scopeTokensMaps = new LinkedHashMap<>();
		Map<Integer, Boolean> prevScopeTokensMaps = new LinkedHashMap<>();
		boolean sameAsPrev = true;
		for (int level = 1/* deliberately skip scope 0 */; level < scopes.length; level++) {
			String scope = scopes[level];

			if (sameAsPrev) {
				if (level < prevTokenScopesLength && prevTokenScopes[level].equals(scope)) {
					prevScopeTokensMaps = prevTokenScopeTokensMaps.get(level);
					scopeTokensMaps.put(level, prevScopeTokensMaps);
					continue;
				}
				sameAsPrev = false;
			}

			int[] tokens = decodeMap.getTokenIds(scope);
			prevScopeTokensMaps = new LinkedHashMap<>(prevScopeTokensMaps);
			for (int i = 0; i < tokens.length; i++) {
				prevScopeTokensMaps.put(tokens[i], true);
			}
			scopeTokensMaps.put(level, prevScopeTokensMaps);
		}

		decodeMap.prevToken = new TMTokenDecodeData(scopes, scopeTokensMaps);
		return decodeMap.getToken(prevScopeTokensMaps);
	}

	public void addModelTokensChangedListener(IModelTokensChangedListener listener) {
		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	public void removeModelTokensChangedListener(IModelTokensChangedListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	public List<Token> getLineTokens(int lineNumber) {
		return _lines.get(lineNumber).tokens;
	}

	public boolean isLineInvalid(int lineNumber) {
		return _lines.get(lineNumber).isInvalid;
	}

	public IRegion getRegion(int fromLine, int toLine) throws BadLocationException {
		int startOffset = document.getLineOffset(fromLine);
		int endOffset = document.getLineOffset(toLine) + document.getLineLength(toLine);
		return new Region(startOffset, endOffset - startOffset);
	}

	public IDocument getDocument() {
		// TODO Auto-generated method stub
		return document;
	}

	public void dispose() {
		document.removeDocumentListener(listener);

		synchronized (this) {
			// http://dev.eclipse.org/bugs/show_bug.cgi?id=19135
			BackgroundThread bt = fThread;
			fThread = null;
			// bt.cancel();
		}
	}
}
