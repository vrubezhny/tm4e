import fr.opensagres.language.textmate.styles.CSSClassRegistry;

public class CSSClassRegistryTest {

	public static void main(String[] args) {
		CSSClassRegistry registry = new CSSClassRegistry();
		registry.addClass("comment");
		
		String bestClass = registry.findBestClass("comment.block.ts.documentation");
		System.err.println(bestClass);
	}
}
