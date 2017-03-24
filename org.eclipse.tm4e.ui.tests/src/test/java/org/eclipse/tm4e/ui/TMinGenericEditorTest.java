package org.eclipse.tm4e.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.tests.harness.util.DisplayHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

public class TMinGenericEditorTest {
	
	private IEditorDescriptor editorDescriptor;
	private File f;
	private IEditorPart editor;

	@Before
	public void checkHasGenericEditor() {
		editorDescriptor = PlatformUI.getWorkbench().getEditorRegistry().findEditor("org.eclipse.ui.genericeditor.GenericEditor");
		Assume.assumeNotNull(editorDescriptor);
	}
	
	@After
	public void tearDown() {
		editor.getEditorSite().getPage().closeEditor(editor, false);
		f.delete();
	}

	@Test
	public void testTMHighlightInGenericEditor() throws IOException, PartInitException {
		f = File.createTempFile("test" + System.currentTimeMillis(), ".ts");
		FileOutputStream fileOutputStream = new FileOutputStream(f);
		fileOutputStream.write("let a = '';\nlet b = 10;\nlet c = true;".getBytes());
		fileOutputStream.close();
		f.deleteOnExit();
		editor = IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(),
				f.toURI(), editorDescriptor.getId(), true);
		StyledText text = (StyledText)editor.getAdapter(Control.class);
		new DisplayHelper() {
			@Override
			protected boolean condition() {
				return text.getStyleRanges().length > 1;
			}
		}.waitForCondition(text.getDisplay(), 3000);
		Assert.assertTrue(text.getStyleRanges().length > 1);
	}

}
