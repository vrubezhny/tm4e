package org.eclipse.tm4e.ui.model;

import org.eclipse.core.runtime.content.IContentType;

/**
 * 
 * @author azerr
 *
 */
public class ContentTypeInfo {

	private final String fileName;
	private final IContentType[] contentTypes;

	public ContentTypeInfo(String fileName, IContentType[] contentTypes) {
		this.fileName = fileName;
		this.contentTypes = contentTypes;
	}

	public String getFileName() {
		return fileName;
	}

	public IContentType[] getContentTypes() {
		return contentTypes;
	}
}
