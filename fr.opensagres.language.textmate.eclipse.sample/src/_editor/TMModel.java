package _editor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

import fr.opensagres.language.textmate.grammar.IGrammar;
import fr.opensagres.language.textmate.grammar.StackElement;
import fr.opensagres.language.textmate.registry.Registry;

public class TMModel implements IDocumentListener {

	private class ModelLine {
		private boolean isInvalid;
		private List<StackElement> state;

	}

	private final Map<Integer, ModelLine> lines;

	private static IGrammar grammar = null;

	public TMModel(IDocument document) {
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

		document.addDocumentListener(this);

		lines = new HashMap<>();
		int linesLength = document.getNumberOfLines();
		for (int line = 0; line < linesLength; line++) {
			lines.put(line, new ModelLine());
		}
		printLines();
	}

	private int _invalidLineStartIndex;

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		IDocument document = event.getDocument();
		int offset = event.getOffset();
		int length = event.getLength();
		String text = event.getText();
		try {
			int startLine = document.getLineOfOffset(offset);
			int endLine = document.getLineOfOffset(offset + length);
			if (text.length() < length) {
				// delete lines
				if (startLine != endLine) {
					int linesLength = lines.size();
					int nbLinesToDelete = endLine - startLine;
					// lines are deleted
					for (int line = startLine; line < linesLength; line++) {
						if (line == startLine) {
							lines.get(line).isInvalid = true;
						} else if (line <= endLine || (line + nbLinesToDelete >= linesLength)) {
							lines.remove(line);
						} else if (line <= linesLength) {
							ModelLine model = lines.remove(line);
							lines.put(line - nbLinesToDelete, model);
						}
					}
				} else {
					lines.get(startLine).isInvalid = true;
				}
			} else if (text.length() > length) {
				int newLinesLength = document.getNumberOfLines();
				// if (newLinesLength > lines.size()) {
				// Add new lines
				int nbLinesToAdd = endLine - startLine + 1;
				for (int line = newLinesLength; line > startLine; line--) {
					if (line < endLine) {
						lines.put(line, new ModelLine());
					} else {
						ModelLine model = lines.get(line - nbLinesToAdd);
						//if (model == null) {
							model = new ModelLine();
						//}
						lines.put(line, model);
					}
				}
				// }
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		printLines();
	}

	private void printLines() {
		System.err.println("-------------------");
		StringBuilder s = new StringBuilder();
		for (Map.Entry<Integer, ModelLine> line : lines.entrySet()) {
			int lineNumber = line.getKey();
			ModelLine model = line.getValue();
			s.append("\nline=" + lineNumber);
		}
		System.err.println(s);
		System.err.println("-------------------");
		System.err.println();
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		IDocument document = event.getDocument();
		int offset = event.getOffset();
		int length = event.getLength();

		// try {
		// int startLine = document.getLineOfOffset(offset);
		// int endLine = document.getLineOfOffset(offset + length);
		//
		// System.err.println(startLine);
		// System.err.println(endLine);
		//
		// } catch (BadLocationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public void setLineContext(int line, List<StackElement> prevState) {
		ModelLine model = lines.get(line);
		model.state = prevState;
	}

	public List<StackElement> getLineContext(int line) {
		ModelLine l = lines.get(line);
		return l != null ? l.state : null;
	}

	private void _revalidateTokensNow(int toLineNumber) {
		toLineNumber = Math.min(this.lines.size(), toLineNumber);

		int MAX_ALLOWED_TIME = 20, fromLineNumber = this._invalidLineStartIndex + 1, tokenizedChars = 0,
				currentCharsToTokenize = 0, currentEstimatedTimeToTokenize = 0;
		// sw = StopWatch.create(false),
		int elapsedTime;

		// Tokenize at most 1000 lines. Estimate the tokenization speed per
		// character and stop when:
		// - MAX_ALLOWED_TIME is reached
		// - tokenizing the next line would go above MAX_ALLOWED_TIME

		for (int lineNumber = fromLineNumber; lineNumber <= toLineNumber; lineNumber++) {
			elapsedTime = 0; // sw.elapsed();
			if (elapsedTime > MAX_ALLOWED_TIME) {
				// Stop if MAX_ALLOWED_TIME is reached
				toLineNumber = lineNumber - 1;
				break;
			}

			// Compute how many characters will be tokenized for this line
			currentCharsToTokenize = 10; // this.lines.get(lineNumber -
											// 1).text.length;

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

			// this._updateTokensUntilLine(lineNumber, false);
			tokenizedChars += currentCharsToTokenize;
		}

		elapsedTime = 0; // sw.elapsed();

		// if (fromLineNumber <= toLineNumber) {
		// this.emitModelTokensChangedEvent(fromLineNumber, toLineNumber);
		// }

		if (this._invalidLineStartIndex < this.lines.size()) {
			// this._beginBackgroundTokenization();
		}
	}

	protected void _resetTokenizationState() {
		// this._clearTimers();
		// for (let i = 0; i < this._lines.length; i++) {
		// this._lines[i].resetTokenizationState();
		// }

		// this._tokenizationSupport = null;
		// if (!this.isTooLargeForHavingAMode()) {
		// this._tokenizationSupport =
		// TokenizationRegistry.get(this._languageId);
		// }
		//
		// if (this._tokenizationSupport) {
		// let initialState: IState = null;
		// try {
		// initialState = this._tokenizationSupport.getInitialState();
		// } catch (e) {
		// e.friendlyMessage = TextModelWithTokens.MODE_TOKENIZATION_FAILED_MSG;
		// onUnexpectedError(e);
		// this._tokenizationSupport = null;
		// }
		//
		// if (initialState) {
		// this._lines[0].setState(initialState);
		// }
		// }
		//
		// this._lastState = null;
		// this._tokensInflatorMap = new TokensInflatorMap(this.getModeId());
		this._invalidLineStartIndex = 0;
		// this._beginBackgroundTokenization();
	}

	// private void _updateTokensUntilLine(int lineNumber, boolean emitEvents) {
	//// if (!this._tokenizationSupport) {
	//// this._invalidLineStartIndex = this._lines.length;
	//// return;
	//// }
	//
	// int linesLength = this.lines.size();
	// int endLineIndex = lineNumber - 1;
	// int stopLineTokenizationAfter = 1000000000; // 1 billion, if a line is so
	// long, you have other trouble :).
	//
	// int fromLineNumber = this._invalidLineStartIndex + 1, toLineNumber =
	// lineNumber;
	//
	// // Validate all states up to and including endLineIndex
	// for (int lineIndex = this._invalidLineStartIndex; lineIndex <=
	// endLineIndex; lineIndex++) {
	// int endStateIndex = lineIndex + 1;
	// ITokenizeLineResult r = null;
	// String text = ""; //this._lines[lineIndex].text;
	//
	// try {
	// // Tokenize only the first X characters
	// r = this.tokenize(this._lines[lineIndex].text,
	// this._lines[lineIndex].getState(), 0, stopLineTokenizationAfter);
	// } catch (Throwable e) {
	// //e.friendlyMessage = TextModelWithTokens.MODE_TOKENIZATION_FAILED_MSG;
	// //onUnexpectedError(e);
	// }
	//
	// if (r && r.tokens && r.tokens.length > 0) {
	// // Cannot have a stop offset before the last token
	// r.actualStopOffset = Math.max(r.actualStopOffset,
	// r.tokens[r.tokens.length - 1].startIndex + 1);
	// }
	//
	// if (r && r.actualStopOffset < text.length) {
	// // Treat the rest of the line (if above limit) as one default token
	// r.tokens.push(new Token(r.actualStopOffset, ""));
	//
	// // Use as end state the starting state
	// r.endState = this._lines[lineIndex].getState();
	// }
	//
	//// if (!r) {
	//// r = nullTokenize(this.getModeId(), text,
	// this._lines[lineIndex].getState());
	//// }
	//// if (!r.modeTransitions) {
	//// r.modeTransitions = [];
	//// }
	//// if (r.modeTransitions.length === 0) {
	//// // Make sure there is at least the transition to the top-most mode
	//// r.modeTransitions.push(new ModeTransition(0, this.getModeId()));
	//// }
	// this._lines[lineIndex].setTokens(this._tokensInflatorMap, r.tokens,
	// r.modeTransitions);
	// this._lines[lineIndex].isInvalid = false;
	//
	// if (endStateIndex < linesLength) {
	// if (this.lines.get(endStateIndex).getState() != null &&
	// r.endState.equals(this._lines[endStateIndex].getState())) {
	// // The end state of this line remains the same
	// var nextInvalidLineIndex = lineIndex + 1;
	// while (nextInvalidLineIndex < linesLength) {
	// if (this.lines[nextInvalidLineIndex].isInvalid) {
	// break;
	// }
	// if (nextInvalidLineIndex + 1 < linesLength) {
	// if (this.lines[nextInvalidLineIndex + 1].getState() == null) {
	// break;
	// }
	// } else {
	// if (this._lastState == null) {
	// break;
	// }
	// }
	// nextInvalidLineIndex++;
	// }
	// this._invalidLineStartIndex = Math.max(this._invalidLineStartIndex,
	// nextInvalidLineIndex);
	// lineIndex = nextInvalidLineIndex - 1; // -1 because the outer loop
	// increments it
	// } else {
	// this._lines[endStateIndex].setState(r.endState);
	// }
	// } else {
	// this._lastState = r.endState;
	// }
	// }
	// this._invalidLineStartIndex = Math.max(this._invalidLineStartIndex,
	// endLineIndex + 1);
	//
	// if (emitEvents && fromLineNumber <= toLineNumber) {
	// //this.emitModelTokensChangedEvent(fromLineNumber, toLineNumber);
	// }
	// }
	//
	// protected void _invalidateLine(int lineIndex) {
	// this.lines.get(lineIndex).isInvalid = true;
	// if (lineIndex < this._invalidLineStartIndex) {
	// if (this._invalidLineStartIndex < this.lines.size()) {
	// ModelLine l = new ModelLine();
	// l.isInvalid = true;
	// this.lines.put(this._invalidLineStartIndex, l);
	// }
	// this._invalidLineStartIndex = lineIndex;
	// this._beginBackgroundTokenization();
	// }
	// }
	//
	// private void _beginBackgroundTokenization() {
	// // if (this._shouldAutoTokenize() && this._revalidateTokensTimeout ===
	// // -1) {
	// // this._revalidateTokensTimeout = setTimeout(() => {
	// // this._revalidateTokensTimeout = -1;
	// this._revalidateTokensNow(this._invalidLineStartIndex + 1000000);
	// // }, 0);
	// // }
	// }
	//
	// public ITokenizeLineResult tokenize(String line, List<StackElement>
	// state, int offsetDelta, int stopAtOffset) {
	// // Do not attempt to tokenize if a line has over 20k
	// // or if the rule stack contains more than 100 rules (indicator of broken
	// grammar that forgets to pop rules)
	//// if (line.length >= 20000 || depth(state.getRuleStack()) > 100) {
	//// return new LineTokens(
	//// [new Token(offsetDelta, '')],
	//// [new ModeTransition(offsetDelta, state.getModeId())],
	//// offsetDelta,
	//// state
	//// );
	//// }
	// List<StackElement> freshState = new ArrayList(state); // .clone();
	// ITokenizeLineResult textMateResult = this.grammar.tokenizeLine(line,
	// freshState);
	// freshState = textMateResult.getRuleStack();
	//
	// return textMateResult;
	// // Create the result early and fill in the tokens later
	//// let tokens: Token[] = [];
	////
	//// let lastTokenType: string = null;
	//// for (let tokenIndex = 0, len = textMateResult.tokens.length; tokenIndex
	// < len; tokenIndex++) {
	//// let token = textMateResult.tokens[tokenIndex];
	//// let tokenStartIndex = token.startIndex;
	//// let tokenType = decodeTextMateToken(this._decodeMap, token.scopes);
	////
	//// // do not push a new token if the type is exactly the same (also helps
	// with ligatures)
	//// if (tokenType !== lastTokenType) {
	//// tokens.push(new Token(tokenStartIndex + offsetDelta, tokenType));
	//// lastTokenType = tokenType;
	//// }
	//// }
	////
	//// return new LineTokens(
	//// tokens,
	//// [new ModeTransition(offsetDelta, freshState.getModeId())],
	//// offsetDelta + line.length,
	//// freshState
	//// );
	// }
}
