/**
 *  Copyright (c) 2015-2017 Angelo ZERR.
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
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.tm4e.core.grammar.IGrammar;

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
	private TokenizerThread fThread;

	private final LineList lines;
	private int _invalidLineStartIndex;

	public AbstractTMModel() {
		this.listeners = new ArrayList<>();
		this.lines = new LineList(lineNumber -> {
			try {
				return getLineText(lineNumber);
			} catch (Exception ex) {
				ex.printStackTrace();
				return "";
			}
		});
	}

	/**
	 * The {@link TokenizerThread} takes as input an {@link AbstractTMModel} and continuously
	 * runs tokenizing in background on the lines found in {@link AbstractTMModel#lines}.
	 * The {@link AbstractTMModel#lines} are expected to be accessed through {@link AbstractTMModel#getLines()}
	 * and manipulated by the UI part to inform of needs to (re)tokenize area, then the {@link TokenizerThread}
	 * processes them and emits events through the model. UI elements are supposed to subscribe and react to the events with
	 * {@link AbstractTMModel#addModelTokensChangedListener(IModelTokensChangedListener)}.
	 *
	 */
	static class TokenizerThread extends Thread {
		private AbstractTMModel model;
		private TMState _lastState;
		private Tokenizer tokenizer;

		/**
		 * Creates a new background thread. The thread runs with minimal
		 * priority.
		 *
		 * @param name
		 *            the thread's name
		 */
		public TokenizerThread(String name, AbstractTMModel model) {
			super(name);
			this.model = model;
			this.tokenizer = new Tokenizer(model.getGrammar());
			setPriority(Thread.MIN_PRIORITY);
			setDaemon(true);
		}

		@Override
		public void run() {
			if (isInterrupted())
				return;

			// initialize
			int linesLength = model.getNumberOfLines();
			for (int line = 0; line < linesLength; line++) {
				try {
					model.lines.addLine(line);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			model.lines.forEach(ModelLine::resetTokenizationState);
			model.lines.get(0).setState(tokenizer.getInitialState());
			model._invalidLineStartIndex = 0;
			
			boolean init = true;
			while (!isInterrupted()) {
				if (!init /* && toProcess.isEmpty() */) { //need someplace to store which lines to process
					synchronized (model.lines) {
						try {
							model.lines.wait(500); // release synchronized lock at least every 500ms
						} catch (InterruptedException x) {
							interrupt();
						}
					}
				}
				init = false;
				while (model.lines.getSize() >= 0 && model._invalidLineStartIndex < model.lines.getSize()) {
					if (interrupted()) {
						return;
					}
					this._revalidateTokensNow(null);
				}
			}

		}

		private void _revalidateTokensNow(Integer toLineNumberOrNull) {
			model._withModelTokensChangedEventBuilder((eventBuilder) -> {
				Integer toLineNumber = toLineNumberOrNull;
				if (toLineNumber == null) {
					toLineNumber = model._invalidLineStartIndex + 1000000;
				}
				toLineNumber = Math.min(model.lines.getSize(), toLineNumber);

				int fromLineNumber = model._invalidLineStartIndex + 1;

				long tokenizedChars = 0;
				long currentCharsToTokenize = 0;
				// sw = StopWatch.create(false),
				final long MAX_ALLOWED_TIME = 20;
				long currentEstimatedTimeToTokenize = 0;
				long elapsedTime;
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
						currentCharsToTokenize = model.getLineLength(lineNumber - 1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // this.lines.get(lineNumber - 1).getLength();

					if (tokenizedChars > 0) {
						// If we have enough history, estimate how long tokenizing this line would take
						currentEstimatedTimeToTokenize = (long) ((double)elapsedTime / tokenizedChars) * currentCharsToTokenize;
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
			});

		}

		private void _updateTokensUntilLine(ModelTokensChangedEventBuilder eventBuilder, int lineNumber,
				boolean emitEvents) {
			int linesLength = model.lines.getSize();
			int endLineIndex = lineNumber - 1;
			int stopLineTokenizationAfter = 1000000000; // 1 billion, if a line is
														// so long, you have other
														// trouble :).
			// Validate all states up to and including endLineIndex
			for (int lineIndex = model._invalidLineStartIndex; lineIndex <= endLineIndex; lineIndex++) {
				int endStateIndex = lineIndex + 1;
				LineTokens r = null;
				String text = null;
				ModelLine modeLine = model.lines.get(lineIndex);
				try {
					text = model.getLineText(lineIndex);
					// Tokenize only the first X characters
					r = tokenizer.tokenize(text, modeLine.getState(), 0, stopLineTokenizationAfter);
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
					// Treat the rest of the line (if above limit) as one default token
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
					ModelLine endStateLine = model.lines.get(endStateIndex);
					if (endStateLine.getState() != null && r.endState.equals(endStateLine.getState())) {
						// The end state of this line remains the same
						int nextInvalidLineIndex = lineIndex + 1;
						while (nextInvalidLineIndex < linesLength) {
							if (model.lines.get(nextInvalidLineIndex).isInvalid) {
								break;
							}
							if (nextInvalidLineIndex + 1 < linesLength) {
								if (model.lines.get(nextInvalidLineIndex + 1).getState() == null) {
									break;
								}
							} else {
								if (this._lastState == null) {
									break;
								}
							}
							nextInvalidLineIndex++;
						}
						model._invalidLineStartIndex = Math.max(model._invalidLineStartIndex, nextInvalidLineIndex);
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
			model._invalidLineStartIndex = Math.max(model._invalidLineStartIndex, endLineIndex + 1);
		}

		private LineTokens nullTokenize(String buffer, TMState state) {
			int deltaOffset = 0;
			List<TMToken> tokens = new ArrayList<>();
			tokens.add(new TMToken(deltaOffset, ""));

			return new LineTokens(tokens, deltaOffset + buffer.length(), state);
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
		if (this.fThread == null || this.fThread.isInterrupted()) {
			this.fThread = new TokenizerThread(getClass().getName(), this);
		}
		if (!this.fThread.isAlive()) {
			this.fThread.start();
		}
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	@Override
	public void removeModelTokensChangedListener(IModelTokensChangedListener listener) {
		listeners.remove(listener);
		if (listeners.isEmpty()) {
			// no need to keep tokenizing if no-one cares
			this.fThread.interrupt();
			this.fThread = null;
		}
	}

	@Override
	public void dispose() {
		this.fThread.interrupt();
		this.fThread = null;
	}

	private void _withModelTokensChangedEventBuilder(Consumer<ModelTokensChangedEventBuilder> callback) {
		ModelTokensChangedEventBuilder eventBuilder = new ModelTokensChangedEventBuilder(this);

		callback.accept(eventBuilder);

		ModelTokensChangedEvent e = eventBuilder.build();
		if (e != null) {
			this.emit(e);
		}
	}

	private void emit(ModelTokensChangedEvent e) {
		for (IModelTokensChangedListener listener : listeners) {
			listener.modelTokensChanged(e);
		}
	}

	public List<TMToken> getLineTokens(int lineNumber) {
		return lines.get(lineNumber).tokens;
	}

	public boolean isLineInvalid(int lineNumber) {
		return lines.get(lineNumber).isInvalid;
	}
	
	protected void invalidateLine(int lineIndex) {
		this.lines.get(lineIndex).isInvalid = true;
		if (lineIndex < this._invalidLineStartIndex) {
			if (this._invalidLineStartIndex < this.lines.getSize()) {
				this.lines.get(this._invalidLineStartIndex).isInvalid = true;
			}
			this._invalidLineStartIndex = lineIndex;
		}
	}
	
	public IModelLines getLines() {
		return this.lines;
	}

	protected abstract int getNumberOfLines();

	protected abstract String getLineText(int line) throws Exception;

	protected abstract int getLineLength(int line) throws Exception;
}
