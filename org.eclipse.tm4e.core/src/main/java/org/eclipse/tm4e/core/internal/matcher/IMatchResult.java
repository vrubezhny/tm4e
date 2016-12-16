package org.eclipse.tm4e.core.internal.matcher;

import org.eclipse.tm4e.core.internal.oniguruma.IOnigCaptureIndex;

public interface IMatchResult {

	IOnigCaptureIndex[] getCaptureIndices();

	int getMatchedRuleId();

}
