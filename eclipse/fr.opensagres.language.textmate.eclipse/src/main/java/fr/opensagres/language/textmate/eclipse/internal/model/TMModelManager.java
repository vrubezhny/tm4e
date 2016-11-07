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
package fr.opensagres.language.textmate.eclipse.internal.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;

import fr.opensagres.language.textmate.core.model.ITMModel;
import fr.opensagres.language.textmate.eclipse.model.ITMModelManager;

/**
 * TextMate model manager which connect/disconnect a TextModel model
 * {@link ITMModel} with an Eclipse {@link IDocument}.
 */
public class TMModelManager implements ITMModelManager {

	private static final ITMModelManager INSTANCE = new TMModelManager();

	public static ITMModelManager getInstance() {
		return INSTANCE;
	}

	private final Map<IDocument, ITMModel> models;

	private TMModelManager() {
		models = new HashMap<>();
	}

	@Override
	public ITMModel connect(IDocument document) {
		ITMModel model = models.get(document);
		if (model != null) {
			return model;
		}
		model = new TMModel(document);
		models.put(document, model);
		return model;
	}

	@Override
	public void disconnect(IDocument document) {
		ITMModel model = models.remove(document);
		if (model != null) {
			model.dispose();
		}
	}
}
