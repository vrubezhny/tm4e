package fr.opensagres.language.textmate.oniguruma;

import java.util.ArrayList;
import java.util.List;

import org.jcodings.specific.UTF8Encoding;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;
import org.joni.Syntax;
import org.joni.WarnCallback;

public class OnigSearcher {

	private final List<Regex> regExps;

	public OnigSearcher(String[] regexps) {
		this.regExps = new ArrayList<Regex>();
		for (int i = 0; i < regexps.length; i++) {
			byte[] pattern = regexps[i].getBytes();
			this.regExps.add(new Regex(pattern, 0, pattern.length, Option.DEFAULT, UTF8Encoding.INSTANCE,
					Syntax.DEFAULT, WarnCallback.DEFAULT));
		}
	}

	public OnigResult search(String lin, int pos) {
		byte[] source = lin.getBytes();
		return search(source, pos);
	}

	public OnigResult search(byte[] source, int pos) {
		int byteOffset = pos;

		int bestLocation = 0;
		OnigResult bestResult = null;
		int index = 0;

		for (Regex regExp : regExps) {
			Matcher matcher = regExp.matcher(source);
			int result = matcher.search(byteOffset, source.length, Option.DEFAULT);
			if (result != -1) {
				int location = matcher.getBegin();
				if (bestResult == null || location < bestLocation) {
					bestLocation = location;
					Region region = matcher.getEagerRegion();
					if (bestResult == null) {
						bestResult = new OnigResult(index, region);
					} else {
						bestResult.update(index, region);
					}
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
