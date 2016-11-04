package fr.opensagres.language.textmate.core.grammar;

import fr.opensagres.language.textmate.core.internal.oniguruma.IOnigCaptureIndex;

public interface IMatchResult {

	IOnigCaptureIndex[] getCaptureIndices();

	int getMatchedRuleId();

}
