package org.eclipse.language.textmate.core.grammar;

import org.eclipse.language.textmate.core.internal.oniguruma.IOnigCaptureIndex;

public interface IMatchResult {

	IOnigCaptureIndex[] getCaptureIndices();

	int getMatchedRuleId();

}
