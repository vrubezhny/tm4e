package _editor.editors.tm;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.text.IDocument;

public class TMModelManager {

	private static final TMModelManager INSTANCE = new TMModelManager();

	public static TMModelManager getInstance() {
		return INSTANCE;
	}

	private final Map<IDocument, TMModel> models;

	private TMModelManager() {
		models = new HashMap<>();
	}

	public TMModel connect(IDocument document, boolean lazyLoad) {

		TMModel model = models.get(document);
		if (model != null) {
			return model;
		}
		model = new TMModel(document, lazyLoad);
		models.put(document, model);
		return model;
	}

	public void disconnect(IDocument document) {
		TMModel model = models.remove(document);
		if (model != null) {
			model.dispose();
		}
	}
}
