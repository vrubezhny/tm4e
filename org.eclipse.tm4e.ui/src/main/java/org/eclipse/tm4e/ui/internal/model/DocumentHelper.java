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
package org.eclipse.tm4e.ui.internal.model;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;

/**
 * Utilities class for {@link IDocument}.
 *
 */
public class DocumentHelper {

	/**
	 * Returns the number of lines in this document.
	 * <p>
	 * Note that a document always has at least one line.
	 * </p>
	 * 
	 * @return the number of lines in this document.
	 */
	public static int getNumberOfLines(IDocument document) {
		return document.getNumberOfLines();
	}

	public static int getStartLine(DocumentEvent event) throws BadLocationException {
		return event.getDocument().getLineOfOffset(event.getOffset());
	}

	public static int getEndLine(DocumentEvent event, boolean documentAboutToBeChanged) throws BadLocationException {
		int length = documentAboutToBeChanged ? event.getLength() : event.getText().length();
		return event.getDocument().getLineOfOffset(event.getOffset() + length);
	}

	public static boolean isRemove(DocumentEvent event) {
		return event.getText() == null || event.getText().length() == 0;
	}

	public static boolean isInsert(DocumentEvent event) {
		return event.getLength() == 0 && event.getText() != null;
	}

	public static String getLineText(IDocument document, int line, boolean withLineDelimiter) throws BadLocationException {
		int lo = document.getLineOffset(line);
		int ll = document.getLineLength(line);
		if (!withLineDelimiter) {
			String delim = document.getLineDelimiter(line);
			ll = ll - (delim != null ? delim.length() : 0);
		}
		return document.get(lo, ll);
	}

	public static int getLineLength(IDocument document, int line) throws BadLocationException {
		//String delim = document.getLineDelimiter(line);
		return document.getLineLength(line); // - (delim != null ? delim.length() : 0);
	}

	public static IRegion getRegion(IDocument document, int fromLine, int toLine) throws BadLocationException {
		int startOffset = document.getLineOffset(fromLine);
		int endOffset = document.getLineOffset(toLine) + document.getLineLength(toLine);
		return new Region(startOffset, endOffset - startOffset);
	}

	/**
	 * Returns the content type from the given {@link IDocument}.
	 * 
	 * @throws CoreException
	 */
	public static IContentType[] getContentTypes(IDocument document) throws CoreException {
		ITextFileBufferManager bufferManager = FileBuffers.getTextFileBufferManager();
		ITextFileBuffer buffer = bufferManager.getTextFileBuffer(document);
		return getContentTypes(buffer);
	}

	private static IContentType[] getContentTypes(ITextFileBuffer buffer) throws CoreException {
		try {
			String fileName = buffer.getFileStore().getName();
			if (buffer.isDirty()) {
				InputStream input = null;
				try {
					input = new DocumentInputStream(buffer.getDocument());
					IContentType[] contentTypes = Platform.getContentTypeManager().findContentTypesFor(input,
							fileName);
					if (contentTypes != null)
						return contentTypes;
				} finally {
					try {
						if (input != null)
							input.close();
					} catch (IOException x) {
					}
				}
			}

			InputStream contents = null;
			try  {
				contents = getContents(buffer);
				return Platform.getContentTypeManager().findContentTypesFor(contents, fileName);
			} catch (Throwable e) {
				if (contents != null) {
					contents.close();
				}
				e.printStackTrace();
				return null;
			}
		} catch (IOException x) {
//			throw new CoreException(new Status(IStatus.ERROR, FileBuffersPlugin.PLUGIN_ID, IStatus.OK,
//					NLSUtility.format(FileBuffersMessages.FileBuffer_error_queryContentDescription,
//							fFile.getFullPath().toOSString()),
//					x));
			x.printStackTrace();
			return null;
		}
	}

	private static InputStream getContents(ITextFileBuffer buffer) throws CoreException {
		IWorkspaceRoot workspaceRoot= ResourcesPlugin.getWorkspace().getRoot();
		IFile file= workspaceRoot.getFile(buffer.getLocation());
		if (file.exists()) {
			return file.getContents();
		}
		return buffer.getFileStore().openInputStream(EFS.NONE, null);
	}
}
