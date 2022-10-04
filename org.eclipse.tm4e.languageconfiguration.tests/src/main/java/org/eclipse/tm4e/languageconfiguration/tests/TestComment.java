/**
 * Copyright (c) 2021, 2022 Red Hat Inc. and others.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.tm4e.languageconfiguration.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.tm4e.languageconfiguration.internal.ToggleLineCommentHandler;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


public class TestComment {

	@AfterEach
	public void tearDown() throws Exception {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
		for (final IProject p : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			p.delete(true, null);
		}
	}

	@Test
	public void testToggleLineCommentUseBlockComment() throws Exception {
		final var now = System.currentTimeMillis();
		final var proj = ResourcesPlugin.getWorkspace().getRoot().getProject(getClass().getName() + now);
		proj.create(null);
		proj.open(null);
		final var file = proj.getFile("whatever.noLineComment");
		file.create(new ByteArrayInputStream("a\n\nb\n\nc".getBytes()), true, null);
		final var editor = (ITextEditor) IDE.openEditor(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file,
				"org.eclipse.ui.genericeditor.GenericEditor");
		final var doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		final var service = PlatformUI.getWorkbench().getService(IHandlerService.class);
		String text = doc.get();
		editor.getSelectionProvider().setSelection(new TextSelection(0, text.length()));
		service.executeCommand(ToggleLineCommentHandler.TOGGLE_LINE_COMMENT_COMMAND_ID, null);
		assertEquals("/*a*/\n\n/*b*/\n\n/*c*/", doc.get());
		
		// Repeatedly executed toggle comment command should remove the comments inserted previously
		text = doc.get();
		editor.getSelectionProvider().setSelection(new TextSelection(0,text.length()));
		service.executeCommand(ToggleLineCommentHandler.TOGGLE_LINE_COMMENT_COMMAND_ID, null);
		assertEquals("a\n\nb\n\nc", doc.get());
	}
	
	/**
	 * Test case for https://github.com/eclipse/wildwebdeveloper/issues/909
	 * @throws Exception
	 */
	@Test
	public void testToggleLineCommentUseBlockCommentAndWindowsEOL() throws Exception {
		final var now = System.currentTimeMillis();
		final var proj = ResourcesPlugin.getWorkspace().getRoot().getProject(getClass().getName() + now);
		proj.create(null);
		proj.open(null);
		final var file = proj.getFile("whatever.noLineComment");
		file.create(new ByteArrayInputStream("a\r\n\r\nb\r\n\r\nc".getBytes()), true, null);
		final var editor = (ITextEditor) IDE.openEditor(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file,
				"org.eclipse.ui.genericeditor.GenericEditor");
		final var doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
		final var service = PlatformUI.getWorkbench().getService(IHandlerService.class);
		String text = doc.get();
		editor.getSelectionProvider().setSelection(new TextSelection(0, 0)); // No matter the selection length
		service.executeCommand(ToggleLineCommentHandler.TOGGLE_LINE_COMMENT_COMMAND_ID, null);
		assertEquals("/*a*/\r\n\r\nb\r\n\r\nc", doc.get());
		
		// Repeatedly executed toggle comment command should remove the comments inserted previously
		text = doc.get();
		editor.getSelectionProvider().setSelection(new TextSelection(0, 0)); // No matter the selection length
		service.executeCommand(ToggleLineCommentHandler.TOGGLE_LINE_COMMENT_COMMAND_ID, null);
		assertEquals("a\r\n\r\nb\r\n\r\nc", doc.get());
	}
}
