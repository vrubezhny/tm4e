package fr.opensagres.language.textmate.core.internal.oniguruma;

public interface IOnigNextMatchResult {

	int getIndex();

	IOnigCaptureIndex[] getCaptureIndices();
}
