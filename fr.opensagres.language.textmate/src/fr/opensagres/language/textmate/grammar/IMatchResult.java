package fr.opensagres.language.textmate.grammar;

import fr.opensagres.language.textmate.oniguruma.IOnigCaptureIndex;

public interface IMatchResult {

	IOnigCaptureIndex[] getCaptureIndices();

	int getMatchedRuleId();

}
