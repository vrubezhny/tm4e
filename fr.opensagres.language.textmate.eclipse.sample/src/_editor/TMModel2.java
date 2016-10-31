package _editor;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentRewriteSession;
import org.eclipse.jface.text.ILineTracker;
import org.eclipse.jface.text.ILineTrackerExtension;
import org.eclipse.jface.text.IRegion;

public class TMModel2 implements ILineTracker, ILineTrackerExtension {

	private final ILineTracker tracker;

	public String[] getLegalLineDelimiters() {
		return tracker.getLegalLineDelimiters();
	}

	public String getLineDelimiter(int line) throws BadLocationException {
		return tracker.getLineDelimiter(line);
	}

	public int computeNumberOfLines(String text) {
		return tracker.computeNumberOfLines(text);
	}

	public int getNumberOfLines() {
		return tracker.getNumberOfLines();
	}

	public int getNumberOfLines(int offset, int length) throws BadLocationException {
		return tracker.getNumberOfLines(offset, length);
	}

	public int getLineOffset(int line) throws BadLocationException {
		return tracker.getLineOffset(line);
	}

	public int getLineLength(int line) throws BadLocationException {
		return tracker.getLineLength(line);
	}

	public int getLineNumberOfOffset(int offset) throws BadLocationException {
		return tracker.getLineNumberOfOffset(offset);
	}

	public IRegion getLineInformationOfOffset(int offset) throws BadLocationException {
		return tracker.getLineInformationOfOffset(offset);
	}

	public IRegion getLineInformation(int line) throws BadLocationException {
		return tracker.getLineInformation(line);
	}

	public void replace(int offset, int length, String text) throws BadLocationException {
		tracker.replace(offset, length, text);
	}

	public void set(String text) {
		tracker.set(text);
	}

	public TMModel2(ILineTracker tracker) {
		this.tracker = tracker;
	}

	@Override
	public void startRewriteSession(DocumentRewriteSession session) throws IllegalStateException {
		if (tracker instanceof ILineTrackerExtension) {
			((ILineTrackerExtension) tracker).startRewriteSession(session);
		}
	}

	@Override
	public void stopRewriteSession(DocumentRewriteSession session, String text) {
		if (tracker instanceof ILineTrackerExtension) {
			((ILineTrackerExtension) tracker).stopRewriteSession(session, text);
		}
	}
}
