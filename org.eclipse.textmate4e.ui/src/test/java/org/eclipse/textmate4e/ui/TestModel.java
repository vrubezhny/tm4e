package org.eclipse.textmate4e.ui;

import java.util.List;

import org.eclipse.jface.text.Document;
import org.eclipse.textmate4e.core.model.IModelTokensChangedListener;
import org.eclipse.textmate4e.core.model.ITMModel;
import org.eclipse.textmate4e.core.model.TMToken;
import org.eclipse.textmate4e.ui.internal.model.TMModel;
import org.junit.Test;

public class TestModel {

	@Test
	public void test() throws InterruptedException {
		final Document document = new Document();
		document.set("var a;");
		TMModel model = new TMModel(document);
		model.addModelTokensChangedListener(new IModelTokensChangedListener() {

			@Override
			public void modelTokensChanged(int i, int j, ITMModel model) {
				System.err.println(i);
			}
		});
		document.set("");
		document.set("var b;");

		List<TMToken> tokens = model.getLineTokens(0);
		System.err.println(tokens);
		model.join();
	}
}
