package fr.opensagres.language.textmate.registry;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import fr.opensagres.language.textmate.grammar.IGrammar;
import fr.opensagres.language.textmate.grammar.reader.GrammarReader;
import fr.opensagres.language.textmate.types.IRawGrammar;

public class Registry {

	// private static IGrammarInfo _extractInfo(IRawGrammar rawGrammar) {
	// return {
	// fileTypes: rawGrammar.fileTypes,
	// name: rawGrammar.name,
	// scopeName: rawGrammar.scopeName,
	// firstLineMatch: rawGrammar.firstLineMatch
	// };
	// }
	//
	// public static readGrammarInfo(path:string, callback:(err:any,
	// grammarInfo:IGrammarInfo)=>void): void {
	// readGrammar(path, (err, grammar) => {
	// if (err) {
	// callback(err, null);
	// return;
	// }
	// callback(null, this._extractInfo(grammar));
	// });
	// }
	//
	// public static readGrammarInfoSync(path:string): IGrammarInfo {
	// return this._extractInfo(readGrammarSync(path));
	// }

	private static final IGrammarLocator DEFAULT_LOCATOR = new IGrammarLocator() {
		
		@Override
		public String getFilePath(String scopeName) {
			return null;
		}
	};
	
	private final IGrammarLocator _locator;
	private final SyncRegistry _syncRegistry;

	public Registry() {
		this(DEFAULT_LOCATOR);
	}

	public Registry(IGrammarLocator locator) {
		this._locator = locator;
		this._syncRegistry = new SyncRegistry();
	}

//	public loadGrammar(String initialScopeName, callback:(err:any, grammar:IGrammar)=>void): void {
//
//		let remainingScopeNames = [ initialScopeName ];
//
//		let seenScopeNames : {[name:string]: boolean;} = {};
//		seenScopeNames[initialScopeName] = true;
//
//		while (remainingScopeNames.length > 0) {
//			let scopeName = remainingScopeNames.shift();
//
//			if (this._syncRegistry.lookup(scopeName)) {
//				continue;
//			}
//
//			let filePath = this._locator.getFilePath(scopeName);
//			if (!filePath) {
//				if (scopeName === initialScopeName) {
//					callback(new Error('Unknown location for grammar <' + initialScopeName + '>'), null);
//					return;
//				}
//				continue;
//			}
//
//			try {
//				let grammar = readGrammarSync(filePath);
//
//				let deps = this._syncRegistry.addGrammar(grammar);
//				deps.forEach((dep) => {
//					if (!seenScopeNames[dep]) {
//						seenScopeNames[dep] = true;
//						remainingScopeNames.push(dep);
//					}
//				});
//			} catch(err) {
//				if (scopeName === initialScopeName) {
//					callback(new Error("Unknown location for grammar <" + initialScopeName + ">"), null);
//					return;
//				}
//			}
//		}
//
//		callback(null, this.grammarForScopeName(initialScopeName));
//	}

	public IGrammar loadGrammarFromPathSync(File file) throws Exception {
		return loadGrammarFromPathSync(file.getPath(), new FileInputStream(file));
	}

	public IGrammar loadGrammarFromPathSync(String path, InputStream in) throws Exception {
		IRawGrammar rawGrammar = GrammarReader.readGrammarSync(path, in);
		this._syncRegistry.addGrammar(rawGrammar);
		return this.grammarForScopeName(rawGrammar.getScopeName());
	}

	public IGrammar grammarForScopeName(String scopeName) {
		return this._syncRegistry.grammarForScopeName(scopeName);
	}

}
