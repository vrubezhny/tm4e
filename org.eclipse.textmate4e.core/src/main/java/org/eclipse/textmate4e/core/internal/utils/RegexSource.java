package org.eclipse.textmate4e.core.internal.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.textmate4e.core.internal.oniguruma.IOnigCaptureIndex;

public class RegexSource {

	private static final Pattern CAPTURING_REGEX_SOURCE = Pattern.compile("[$](\\d+)|[${](\\d+):\\/(downcase|upcase)}");

	public static boolean hasCaptures(String regexSource) {
		if (regexSource == null) {
			return false;
		}
		return CAPTURING_REGEX_SOURCE.matcher(regexSource).find();
	}

	public static String replaceCaptures(String regexSource, String captureSource,
			IOnigCaptureIndex[]  captureIndices) {		
		String result = regexSource;
		Matcher m = CAPTURING_REGEX_SOURCE.matcher(regexSource);
		while (m.find()) {
			String match = m.group();
			String replacement = getReplacement(match, captureSource, captureIndices);
			result = result.replaceAll("\\" + match + "", replacement);
			//String replacement = escapeRegExpCharacters(capturedValues.get(index));			
			//result = result.replaceAll("\\" + g1 + "", replacement);
		}
		return result;
		
		
		// return regexSource.replace(CAPTURING_REGEX_SOURCE, (match:string,
		// index:string, commandIndex:string, command:string) => {
		// let capture = captureIndices[parseInt(index || commandIndex, 10)];
		// if (capture) {
		// let result = captureSource.substring(capture.start, capture.end);
		// // Remove leading dots that would make the selector invalid
		// while (result[0] === '.') {
		// result = result.substring(1);
		// }
		// switch (command) {
		// case 'downcase':
		// return result.toLowerCase();
		// case 'upcase':
		// return result.toUpperCase();
		// default:
		// return result;
		// }
		// } else {
		// return match;
		// }
		// });

	}

	private static String getReplacement(String match, String captureSource, IOnigCaptureIndex[] captureIndices) {
		int index = Integer.parseInt(match.substring(1,  match.length()));
		IOnigCaptureIndex capture = captureIndices.length > index ? captureIndices[index] : null;
		if (capture != null) {
			String result = captureSource.substring(capture.getStart(), capture.getEnd());
			// Remove leading dots that would make the selector invalid
			while (result.length() > 0 && result.charAt(0) == '.') {
				result = result.substring(1);
			}
//				switch (command) {
//					case 'downcase':
//						return result.toLowerCase();
//					case 'upcase':
//						return result.toUpperCase();
//					default:
//						return result;
//				}
			return result;
		} else {
			return match;
		}
	}
}
