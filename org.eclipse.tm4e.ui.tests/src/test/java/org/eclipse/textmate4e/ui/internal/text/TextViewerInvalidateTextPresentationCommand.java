package org.eclipse.textmate4e.ui.internal.text;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.TextViewer;

public class TextViewerInvalidateTextPresentationCommand extends Command {

	private final int offset;
	private final int length;
	private final TextViewer viewer;

	public TextViewerInvalidateTextPresentationCommand(int offset, int length, TextViewer viewer) {
		super(getName(offset, length));
		this.offset = offset;
		this.length = length;
		this.viewer = viewer;
	}

	public static String getName(int offset, int length) {
		return "viewer.invalidateTextPresentation(" + offset + ", " + length + ");";
	}

	@Override
	protected void doExecute() {
		viewer.getTextWidget().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				viewer.invalidateTextPresentation(offset, length);
			}
		});
	}

	@Override
	protected Integer getLineTo() {
		try {
			return viewer.getDocument().getLineOfOffset(offset + length);
		} catch (BadLocationException e) {
			return null;
		}
	}

}
