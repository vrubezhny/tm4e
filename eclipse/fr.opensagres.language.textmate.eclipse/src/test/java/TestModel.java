import java.util.List;

import org.eclipse.jface.text.Document;
import org.junit.Test;

import fr.opensagres.language.textmate.core.model.IModelTokensChangedListener;
import fr.opensagres.language.textmate.core.model.ITMModel;
import fr.opensagres.language.textmate.core.model.TMToken;
import fr.opensagres.language.textmate.eclipse.internal.model.TMModel;

public class TestModel {

	@Test
	public void test() throws InterruptedException {
		Document document = new Document();
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
