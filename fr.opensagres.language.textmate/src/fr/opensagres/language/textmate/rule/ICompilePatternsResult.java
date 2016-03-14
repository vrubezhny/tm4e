package fr.opensagres.language.textmate.rule;

public interface ICompilePatternsResult {

	int[] getPatterns();

	boolean hasMissingPatterns();
}
