package org.eclipse.textmate4e.core.grammar;

import org.eclipse.textmate4e.core.internal.oniguruma.IOnigCaptureIndex;

public interface IMatchResult {

	IOnigCaptureIndex[] getCaptureIndices();

	int getMatchedRuleId();

}
