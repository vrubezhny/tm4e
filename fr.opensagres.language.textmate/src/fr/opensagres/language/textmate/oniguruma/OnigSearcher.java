package fr.opensagres.language.textmate.oniguruma;

import java.util.ArrayList;
import java.util.List;

public class OnigSearcher {

	private final List<OnigRegExp> regExps;

	public OnigSearcher(String[] regexps) {
		this.regExps = new ArrayList<OnigRegExp>();
		for (int i = 0; i < regexps.length; i++) {
			this.regExps.add(new OnigRegExp(regexps[i]));
		}
	}

	public OnigResult search(OnigString source, int pos) {
		int byteOffset = pos;

		int bestLocation = 0;
		OnigResult bestResult = null;
		int index = 0;

		for (OnigRegExp regExp : regExps) {
			OnigResult result = regExp.Search(source, pos);
			if (result != null && result.count() > 0) {
				int location = result.LocationAt(0);
				
				if (bestResult == null || location < bestLocation) {
					bestLocation = location;
					bestResult = result;
					bestResult.setIndex(index);
				}

				if (location == byteOffset) {
					break;
				}
			}
			index++;
		}
		return bestResult;
	}

}
