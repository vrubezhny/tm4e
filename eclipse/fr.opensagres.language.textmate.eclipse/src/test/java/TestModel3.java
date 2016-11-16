import java.util.List;

import org.eclipse.jface.text.Document;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.opensagres.language.textmate.core.grammar.IGrammar;
import fr.opensagres.language.textmate.core.model.IModelTokensChangedListener;
import fr.opensagres.language.textmate.core.model.ITMModel;
import fr.opensagres.language.textmate.core.model.TMToken;
import fr.opensagres.language.textmate.core.registry.Registry;
import fr.opensagres.language.textmate.eclipse.internal.model.TMModel;

public class TestModel3 {

	private static IGrammar grammar;

	@BeforeClass
	public static void setUp() throws Exception {
		Registry registry = new Registry();
		grammar = registry.loadGrammarFromPathSync("TypeScript.tmLanguage.json",
				TestModel3.class.getResourceAsStream("TypeScript.tmLanguage.json"));
	}

	@Test
	public void test() throws InterruptedException {
		Document document = new Document();
		document.set("var a;\nvar b;");
		TMModel model = new TMModel(document);
		model.setGrammar(grammar);
		model.addModelTokensChangedListener(new IModelTokensChangedListener() {

			@Override
			public void modelTokensChanged(int i, int j, ITMModel model) {
				System.err.println(j);
				List<TMToken> tokens = model.getLineTokens(j);
				for (TMToken token : tokens) {
					System.err.println("start=" + token.startIndex + ", type=" + token.type);
				}				
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		//		synchronized (model) {
//					model.notifyAll();
//				}
			}
		});
		
		document.set("");

		
		synchronized (model) {
			model.wait(5000);
		}
		
		
	}
}
