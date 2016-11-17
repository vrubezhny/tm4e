package org.eclipse.language.textmate.core;
import java.util.Iterator;

import org.jcodings.specific.UTF8Encoding;
import org.joni.Matcher;
import org.joni.NameEntry;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;

public class TestOnig {

	// [0]:"(<)([_$a-zA-Z][-$\w.]*(?<!\.|-))(>)"
	// [1]:"(?x)\n  (<)\n  ([_$a-zA-Z][-$\w.]*(?<!\.|-))\n  (?=\s+(?!\?)|/?>)"
	public static void main(String[] args) {
		byte[] pattern = "a(b)?".getBytes();
	    byte[] str = "abc".getBytes();

	    Regex regex = new Regex(pattern, 0, pattern.length, Option.DEFAULT, UTF8Encoding.INSTANCE);
	    Matcher matcher = regex.matcher(str);
	    int result = matcher.search(0, str.length, Option.DEFAULT);
	    if (result != -1) {
	        Region region = matcher.getEagerRegion();
	        System.err.println(region);
//	        for (Iterator<NameEntry> entry = regex.namedBackrefIterator(); entry.hasNext();) {
//	            NameEntry e = entry.next();
//	            int number = e.getBackRefs()[0]; // can have many refs per name
//	            int begin = region.beg[number];
//	            int end = region.end[number];
////
//	        }
	    }
	}
}
