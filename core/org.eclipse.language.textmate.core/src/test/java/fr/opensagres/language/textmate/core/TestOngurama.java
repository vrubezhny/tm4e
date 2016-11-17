package fr.opensagres.language.textmate.core;
import fr.opensagres.language.textmate.core.internal.oniguruma.IOnigNextMatchResult;
import fr.opensagres.language.textmate.core.internal.oniguruma.OnigScanner;

public class TestOngurama {

	public static void main(String[] args) {
		
		OnigScanner scanner = new OnigScanner(new String[] {"c", "a(b)?"});
		IOnigNextMatchResult result = scanner._findNextMatchSync("abc", 0);
		System.err.println(result);
				
		scanner = new OnigScanner(new String[] {"a([b-d])c"});
		result = scanner._findNextMatchSync("!abcdef", 0);
		System.err.println(result);
	}
}
