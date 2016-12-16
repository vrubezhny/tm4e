package org.eclipse.tm4e.ui;

import java.util.List;

import org.eclipse.jface.text.Document;
import org.eclipse.tm4e.core.model.ModelTokensChangedEvent;
import org.eclipse.tm4e.core.model.IModelTokensChangedListener;
import org.eclipse.tm4e.core.model.Range;
import org.eclipse.tm4e.core.model.TMToken;
import org.eclipse.tm4e.ui.internal.model.TMModel;
import org.junit.Test;

public class TestModel {

	@Test
	public void test() throws InterruptedException {
		final Document document = new Document();
		document.set("var a;");
		TMModel model = new TMModel(document);
		model.addModelTokensChangedListener(new IModelTokensChangedListener() {

			@Override
			public void modelTokensChanged(ModelTokensChangedEvent e) {
				List<Range> ranges = e.getRanges();
				for (Range range : ranges) {
					System.err
							.println("fromLineNumber=" + range.fromLineNumber + ", toLineNumber=" + range.toLineNumber);
				}
			}
		});
		document.set("");
		document.set("var b;");

		List<TMToken> tokens = model.getLineTokens(0);
		System.err.println(tokens);
		model.join();
	}
}
