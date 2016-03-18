package fr.opensagres.language.textmate.oniguruma;

public interface IOnigNextMatchResult {

	int getIndex();

	IOnigCaptureIndex[] getCaptureIndices();
}
