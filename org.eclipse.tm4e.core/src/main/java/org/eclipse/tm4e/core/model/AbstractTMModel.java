/**
 *  Copyright (c) 2015-2016 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package org.eclipse.tm4e.core.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.tm4e.core.TMException;
import org.eclipse.tm4e.core.grammar.IGrammar;
import org.eclipse.tm4e.core.grammar.IToken;
import org.eclipse.tm4e.core.grammar.ITokenizeLineResult;

/**
 * Abstract class for TextMate model.
 *
 */
public abstract class AbstractTMModel implements ITMModel {

	/**
	 * The TextMate grammar to use to parse for each lines of the document the
	 * TextMate tokens.
	 **/
	private IGrammar grammar;

	/** Listener when TextMate model tokens changed **/
	private final List<IModelTokensChangedListener> listeners;

	/** The background thread. */
	private BackgroundThread fThread;
	/** The background thread delay. */
	private int fDelay = 200;
	/** Queue to manage the changes applied to the text viewer. */
	private IModelLines fDirtyRegionQueue;

	private final LineList lines;

	private boolean initialized;

	private int _invalidLineStartIndex;
	private TMState _lastState;

	private DecodeMap _decodeMap;

	private boolean _isDisposing;

	public AbstractTMModel() {
		this._decodeMap = new DecodeMap();
		this.listeners = new ArrayList<>();
		this.lines = new LineList();
		fDirtyRegionQueue = lines;
		_isDisposing = false;
		fThread = new BackgroundThread(getClass().getName());
	}

	class LineList extends ArrayList<ModelLine> implements IModelLines {

		@Override
		public void addLine(int line) {
			try {
				add(line, new ModelLine(getLineText(line)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void removeLine(int line) {
			remove(line);
		}

		@Override
		public boolean add(ModelLine line) {
			fThread.reset();
			return super.add(line);
		}

		@Override
		public void add(int index, ModelLine element) {
			fThread.reset();
			super.add(index, element);
		}

		@Override
		public ModelLine remove(int index) {
			fThread.reset();
			return super.remove(index);
		}

		@Override
		public void updateLine(int line) {
			fThread.reset();
			try {
				super.get(line).text = getLineText(line);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public int getSize() {
			return size();
		}
	}

	class BackgroundThread extends Thread {

		/** Has the reconciler been canceled. */
		private boolean fCanceled = false;
		/** Has the reconciler been reset. */
		private boolean fReset = false;
		/** Some changes need to be processed. */
		private boolean fIsDirty = false;
		/** Is a reconciling strategy active. */
		private boolean fIsActive = false;

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

		/**
		 * Suspends the caller of this method until this background thread has
		 * emptied the dirty region queue.
		 */
		public void suspendCallerWhileDirty() {
			boolean isDirty;
			do {
				synchronized (fDirtyRegionQueue) {
					isDirty = fDirtyRegionQueue.getSize() > 0;
					if (isDirty) {
						try {
							fDirtyRegionQueue.wait();
						} catch (InterruptedException x) {
						}
					}
				}
			} while (isDirty);
		}

		/**
		 * Reset the background thread as the text viewer has been changed,
		 */
		public void reset() {

			if (fDelay > 0) {

				synchronized (this) {
					fIsDirty = true;
					fReset = true;
				}

			} else {

				synchronized (this) {
					fIsDirty = true;
				}

				synchronized (fDirtyRegionQueue) {
					fDirtyRegionQueue.notifyAll();
				}
			}

			reconcilerReset();
		}

		@Override
		public void run() {
			/*synchronized (fDirtyRegionQueue) {
				try {
					fDirtyRegionQueue.wait(fDelay);
				} catch (InterruptedException x) {
				}
			}*/

			if (fCanceled)
				return;

			initializeIfNeeded();
			_revalidateTokensNow(null);
			// initialProcess();

			while (!fCanceled) {

				synchronized (fDirtyRegionQueue) {
					try {
						fDirtyRegionQueue.wait(fDelay);
					} catch (InterruptedException x) {
					}
				}

				if (fCanceled)
					break;

				if (!isDirty())
					continue;

				synchronized (this) {
					if (fReset) {
						fReset = false;
						continue;
					}
				}

				// DirtyRegion r= null;
				// synchronized (fDirtyRegionQueue) {
				// r= fDirtyRegionQueue.removeNextDirtyRegion();
				// }

				fIsActive = true;

				// fProgressMonitor.setCanceled(false);

				// process(r);

				while (_invalidLineStartIndex < fDirtyRegionQueue.getSize()) {
					synchronized (this) {
						if (fReset || fCanceled) {
							break;
						}
					}
					_revalidateTokensNow(null);
				}

				// System.err.println("START--------------------------");
				// for (ModelLine line : ((List<ModelLine>) getLines())) {
				// System.err.println(line.text);
				// }
				// System.err.println("END--------------------------");

				synchronized (fDirtyRegionQueue) {

					// if (0 == fDirtyRegionQueue.getSize()) {
					// synchronized (this) {
					// fIsDirty= fProgressMonitor.isCanceled();
					// }
					fIsDirty = true; // (_invalidLineStartIndex <
										// fDirtyRegionQueue.getSize());
					fDirtyRegionQueue.notifyAll();
					// }
				}

				fIsActive = false;
			}

		}

		/**
		 * Returns whether a reconciling strategy is active right now.
		 *
		 * @return <code>true</code> if a activity is active
		 */
		public boolean isActive() {
			return fIsActive;
		}

		/**
		 * Returns whether some changes need to be processed.
		 *
		 * @return <code>true</code> if changes wait to be processed
		 * @since 3.0
		 */
		public synchronized boolean isDirty() {
			return fIsDirty;
		}

		/**
		 * Cancels the background thread.
		 */
		public void cancel() {
			fCanceled = true;
			// IProgressMonitor pm= fProgressMonitor;
			// if (pm != null)
			// pm.setCanceled(true);
			// synchronized (fDirtyRegionQueue) {
			// fDirtyRegionQueue.notifyAll();
			// }
		}
	}

	@Override
	public IGrammar getGrammar() {
		return grammar;
	}

	@Override
	public void setGrammar(IGrammar grammar) {
		this.grammar = grammar;
	}

	@Override
	public void addModelTokensChangedListener(IModelTokensChangedListener listener) {
		initializeIfNeeded();
		synchronized (listeners) {
			if (!listeners.contains(listener)) {
				listeners.add(listener);
			}
		}
	}

	@Override
	public void removeModelTokensChangedListener(IModelTokensChangedListener listener) {
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}

	@Override
	public void dispose() {
		synchronized (this) {
			BackgroundThread bt = fThread;
			fThread = null;
			bt.cancel();
		}
	}

	protected void initializeIfNeeded() {
		if (!initialized) {
			initialize();
			initialized = true;
		}
	}

	protected synchronized void initialize() {
		if (initialized) {
			return;
		}
		int linesLength = getNumberOfLines();
		synchronized (lines) {
			for (int line = 0; line < linesLength; line++) {
				try {
					lines.addLine(line);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		startReconciling();
		_resetTokenizationState();
	}

	/**
	 * Starts the reconciler to reconcile the queued dirty-regions. Clients may
	 * extend this method.
	 */
	protected synchronized void startReconciling() {
		if (fThread == null)
			return;

		if (!fThread.isAlive()) {
			try {
				fThread.start();
			} catch (IllegalThreadStateException e) {
				// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=40549
				// This is the only instance where the thread is started; since
				// we checked that it is not alive, it must be dead already due
				// to a run-time exception or error. Exit.
			}
		} else {
			fThread.reset();
		}
	}

	/**
	 * Hook that is called after the reconciler thread has been reset.
	 */
	protected void reconcilerReset() {
	}

	/**
	 * Tells whether the code is running in this reconciler's background thread.
	 *
	 * @return <code>true</code> if running in this reconciler's background
	 *         thread
	 * @since 3.4
	 */
	protected boolean isRunningInReconcilerThread() {
		return Thread.currentThread() == fThread;
	}

	protected void aboutToBeReconciled() {
		// TODO Auto-generated method stub

	}

	public IModelLines getLines() {
		return lines;
	}

	protected void _invalidateLine(int lineIndex) {
		lines.get(lineIndex).isInvalid = true;
		if (lineIndex < this._invalidLineStartIndex) {
			if (this._invalidLineStartIndex < this.lines.size()) {
				this.lines.get(this._invalidLineStartIndex).isInvalid = true;
			}
			this._invalidLineStartIndex = lineIndex;
			this._beginBackgroundTokenization();
		}
	}

	private void _resetTokenizationState() {
		for (ModelLine line : this.lines) {
			line.resetTokenizationState();
		}
		lines.get(0).setState(new TMState(null, null));
		this._invalidLineStartIndex = 0;
		this._beginBackgroundTokenization();
	}

	private void _withModelTokensChangedEventBuilder(Consumer<ModelTokensChangedEventBuilder> callback) {
		ModelTokensChangedEventBuilder eventBuilder = new ModelTokensChangedEventBuilder(this);

		callback.accept(eventBuilder);

		if (!this._isDisposing) {
			ModelTokensChangedEvent e = eventBuilder.build();
			if (e != null) {
				this.emit(e);
			}
		}

		// return result;
	}

	private void _revalidateTokensNow(Integer toLineNumberOrNull) {
		_withModelTokensChangedEventBuilder((eventBuilder) -> {
			Integer toLineNumber = toLineNumberOrNull;
			if (toLineNumber == null) {
				toLineNumber = this._invalidLineStartIndex + 1000000;
			}
			toLineNumber = Math.min(this.lines.size(), toLineNumber);

			int fromLineNumber = this._invalidLineStartIndex + 1;

			// sw = StopWatch.create(false),
			long MAX_ALLOWED_TIME = 20, tokenizedChars = 0, currentCharsToTokenize = 0,
					currentEstimatedTimeToTokenize = 0, elapsedTime;
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
					currentCharsToTokenize = getLineLength(lineNumber - 1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} // this.lines.get(lineNumber - 1).getLength();

				if (tokenizedChars > 0) {
					// If we have enough history, estimate how long tokenizing
					// this
					// line would take
					currentEstimatedTimeToTokenize = (elapsedTime / tokenizedChars) * currentCharsToTokenize;
					if (elapsedTime + currentEstimatedTimeToTokenize > MAX_ALLOWED_TIME) {
						// Tokenizing this line will go above MAX_ALLOWED_TIME
						toLineNumber = lineNumber - 1;
						break;
					}
				}

				this._updateTokensUntilLine(eventBuilder, lineNumber, false);
				tokenizedChars += currentCharsToTokenize;
			}

			elapsedTime = System.currentTimeMillis() - startTime;// sw.elapsed();

			if (this._invalidLineStartIndex < this.lines.size()) {
				this._beginBackgroundTokenization();
			}
		});

	}

	private void _beginBackgroundTokenization() {
		// fThread.reset();
	}

	private void emit(ModelTokensChangedEvent e) {
		for (IModelTokensChangedListener listener : listeners) {
			listener.modelTokensChanged(e);
		}
	}

	private void _updateTokensUntilLine(ModelTokensChangedEventBuilder eventBuilder, int lineNumber,
			boolean emitEvents) {
		int linesLength = this.lines.size();
		int endLineIndex = lineNumber - 1;
		int stopLineTokenizationAfter = 1000000000; // 1 billion, if a line is
													// so long, you have other
													// trouble :).
		// Validate all states up to and including endLineIndex
		for (int lineIndex = this._invalidLineStartIndex; lineIndex <= endLineIndex; lineIndex++) {
			int endStateIndex = lineIndex + 1;
			LineTokens r = null;
			String text = null;
			ModelLine modeLine = this.lines.get(lineIndex);
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
				r.tokens.add(new TMToken(r.actualStopOffset, ""));

				// Use as end state the starting state
				r.endState = modeLine.getState();
			}

			if (r == null) {
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
			eventBuilder.registerChangedTokens(lineIndex + 1);
			modeLine.isInvalid = false;

			if (endStateIndex < linesLength) {
				ModelLine endStateLine = this.lines.get(endStateIndex);
				if (endStateLine.getState() != null && r.endState.equals(endStateLine.getState())) {
					// The end state of this line remains the same
					int nextInvalidLineIndex = lineIndex + 1;
					while (nextInvalidLineIndex < linesLength) {
						if (this.lines.get(nextInvalidLineIndex).isInvalid) {
							break;
						}
						if (nextInvalidLineIndex + 1 < linesLength) {
							if (this.lines.get(nextInvalidLineIndex + 1).getState() == null) {
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
	}

	private LineTokens nullTokenize(String buffer, TMState state) {
		int deltaOffset = 0;
		List<TMToken> tokens = new ArrayList<>();
		tokens.add(new TMToken(deltaOffset, ""));

		return new LineTokens(tokens, deltaOffset + buffer.length(), state);
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

		if (grammar == null) {
			throw new TMException("No TextMate grammar defined");
		}
		TMState freshState = state.clone();
		ITokenizeLineResult textMateResult = grammar.tokenizeLine(line, freshState.getRuleStack());
		freshState.setRuleStack(textMateResult.getRuleStack());

		// Create the result early and fill in the tokens later
		List<TMToken> tokens = new ArrayList<>();
		String lastTokenType = null;
		for (int tokenIndex = 0, len = textMateResult.getTokens().length; tokenIndex < len; tokenIndex++) {
			IToken token = textMateResult.getTokens()[tokenIndex];
			int tokenStartIndex = token.getStartIndex();
			String tokenType = decodeTextMateToken(this._decodeMap, token.getScopes().toArray(new String[0]));

			// do not push a new token if the type is exactly the same (also
			// helps with ligatures)
			if (!tokenType.equals(lastTokenType)) {
				tokens.add(new TMToken(tokenStartIndex + offsetDelta, tokenType));
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

	public List<TMToken> getLineTokens(int lineNumber) {
		_withModelTokensChangedEventBuilder((eventBuilder) -> {
			_updateTokensUntilLine(eventBuilder, lineNumber, true);
		});
		return lines.get(lineNumber).tokens;
	}

	public boolean isLineInvalid(int lineNumber) {
		return lines.get(lineNumber).isInvalid;
	}

	protected abstract int getNumberOfLines();

	protected abstract String getLineText(int line) throws Exception;

	protected abstract int getLineLength(int line) throws Exception;

	public void join() throws InterruptedException {
		fThread.join();
	}
}
