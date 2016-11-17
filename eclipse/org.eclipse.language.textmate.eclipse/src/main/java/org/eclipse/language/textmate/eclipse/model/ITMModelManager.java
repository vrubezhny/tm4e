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
package org.eclipse.language.textmate.eclipse.model;

import org.eclipse.jface.text.IDocument;
import org.eclipse.language.textmate.core.model.ITMModel;

/**
 * TextMate model manager API.
 *
 */
public interface ITMModelManager {

	/**
	 * Connect the given document to a TextMate model.
	 * 
	 * @param document
	 * @return the TextMate model connected to the document.
	 */
	ITMModel connect(IDocument document);

	/**
	 * Disconnect the TextMate model of the given document.
	 * 
	 * @param document
	 */
	void disconnect(IDocument document);
}
