package fr.opensagres.language.textmate.utils;

public class RegexSource {

	//private final String CAPTURING_REGEX_SOURCE = /\$(\d+)|\${(\d+):\/(downcase|upcase)}/;

	
	public static boolean hasCaptures(String regexSource) {
		return false; //CAPTURING_REGEX_SOURCE.test(regexSource);
	}

	public static String replaceCaptures(String regexSource, String captureSource, /*IOnigCaptureIndex[]*/ Object captureIndices){
//		return regexSource.replace(CAPTURING_REGEX_SOURCE, (match:string, index:string, commandIndex:string, command:string) => {
//			let capture = captureIndices[parseInt(index || commandIndex, 10)];
//			if (capture) {
//				let result = captureSource.substring(capture.start, capture.end);
//				// Remove leading dots that would make the selector invalid
//				while (result[0] === '.') {
//					result = result.substring(1);
//				}
//				switch (command) {
//					case 'downcase':
//						return result.toLowerCase();
//					case 'upcase':
//						return result.toUpperCase();
//					default:
//						return result;
//				}
//			} else {
//				return match;
//			}
//		});
		return null;
	}
}
