package fr.opensagres.language.textmate.oniguruma;

public class OnigScanner {

	private final OnigSearcher searcher;

	public OnigScanner(String[] regexps) {
		this.searcher = new OnigSearcher(regexps);
	}

	public IOnigNextMatchResult _findNextMatchSync(String lin, int pos) {
		OnigResult bestResult = searcher.search(lin, pos);
		return bestResult;
	}

	public IOnigNextMatchResult _findNextMatchSync(OnigString lin, int pos) {

		return null;
	}
}
