package fr.opensagres.language.textmate.oniguruma;

public class OnigScanner {

	private final OnigSearcher searcher;

	public OnigScanner(String[] regexps) {
		this.searcher = new OnigSearcher(regexps);

	}

	public IOnigNextMatchResult _findNextMatchSync(OnigString lin, int pos) {
		//long start = System.currentTimeMillis();
		OnigResult bestResult = searcher.search(lin, pos);
		//System.err.println("_findNextMatchSync in " + (System.currentTimeMillis() - start) + "ms");
		return bestResult;
	}

	public IOnigNextMatchResult _findNextMatchSync(String lin, int pos) {
		return _findNextMatchSync(new OnigString(lin), pos);
	}
}
