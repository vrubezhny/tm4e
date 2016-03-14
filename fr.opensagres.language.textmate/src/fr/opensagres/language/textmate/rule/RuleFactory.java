package fr.opensagres.language.textmate.rule;

import java.util.ArrayList;
import java.util.Collection;

import fr.opensagres.language.textmate.types.IRawCaptures;
import fr.opensagres.language.textmate.types.IRawGrammar;
import fr.opensagres.language.textmate.types.IRawRepository;
import fr.opensagres.language.textmate.types.IRawRule;

public class RuleFactory {

	public static CaptureRule createCaptureRule(IRuleFactoryHelper helper, final String name, final String contentName,
			final int retokenizeCapturedWithRuleId) {
		return (CaptureRule) helper.registerRule(new IRuleFactory() {
			@Override
			public Rule create(int id) {
				return new CaptureRule(id, name, contentName, retokenizeCapturedWithRuleId);
			}
		});
	}

	public static int getCompiledRuleId(final IRawRule desc, final IRuleFactoryHelper helper,
			final IRawRepository repository) {
		if (desc.getId() == null) {

			helper.registerRule(new IRuleFactory() {

				@Override
				public Rule create(int id) {
					desc.setId(id);

					if (desc.getMatch() != null) {
						return new MatchRule(desc.getId(), desc.getName(), desc.getMatch(),
								RuleFactory._compileCaptures(desc.getCaptures(), helper, repository));
					}

					if (desc.getBegin() == null) {
						/*
						 * if (desc.getRepository() != null) { repository =
						 * null; //mergeObjects({}, repository,
						 * desc.repository); }
						 */
						return new IncludeOnlyRule(desc.getId(), desc.getName(), desc.getContentName(),
								RuleFactory._compilePatterns(desc.getPatterns(), helper, repository));
					}

					return new BeginEndRule(desc.getId(), desc.getName(), desc.getContentName(), desc.getBegin(),
							RuleFactory._compileCaptures(
									desc.getBeginCaptures() != null ? desc.getBeginCaptures() : desc.getCaptures(),
									helper, repository),
							desc.getEnd(),
							RuleFactory._compileCaptures(
									desc.getEndCaptures() != null ? desc.getEndCaptures() : desc.getCaptures(), helper,
									repository),
							desc.getApplyEndPatternLast(),
							RuleFactory._compilePatterns(desc.getPatterns(), helper, repository));
				}
			});
		}

		return desc.getId();
	}

	private static Collection<CaptureRule> _compileCaptures(IRawCaptures captures, IRuleFactoryHelper helper, IRawRepository repository) {
		Collection<CaptureRule> r = new ArrayList<CaptureRule>();
		int numericCaptureId;
			int maximumCaptureId;
			int i;
			String captureId;

		if (captures != null) {
			// Find the maximum capture id
			maximumCaptureId = 0;
			for (captureId in captures) {
				numericCaptureId = parseInt(captureId, 10);
				if (numericCaptureId > maximumCaptureId) {
					maximumCaptureId = numericCaptureId;
				}
			}

			// Initialize result
			for (i = 0; i <= maximumCaptureId; i++) {
				r[i] = null;
			}

			// Fill out result
			for (captureId in captures) {
				numericCaptureId = parseInt(captureId, 10);
				int retokenizeCapturedWithRuleId = 0;
				if (captures[captureId].patterns) {
					retokenizeCapturedWithRuleId = RuleFactory.getCompiledRuleId(captures[captureId], helper, repository);
				}
				r[numericCaptureId] = RuleFactory.createCaptureRule(helper, captures[captureId].name, captures[captureId].contentName, retokenizeCapturedWithRuleId);
			}
		}

		return r;
	}

	private static ICompilePatternsResult _compilePatterns(IRawRule[] patterns, IRuleFactoryHelper helper, IRawRepository repository) {
		Collection<Integer> r = new ArrayList<Integer>();
				IRawRule pattern ;
			int i;
			int len;
			int patternId;
			IRawGrammar externalGrammar;
			Rule rule;
			boolean skipRule;

		if (patterns != null) {
			for (i = 0, len = patterns.length; i < len; i++) {
				pattern = patterns[i];
				patternId = -1;

				if (pattern.include) {
					if (pattern.include.charAt(0) == '#') {
						// Local include found in `repository`
						let localIncludedRule = repository[pattern.include.substr(1)];
						if (localIncludedRule) {
							patternId = RuleFactory.getCompiledRuleId(localIncludedRule, helper, repository);
						} else {
							// console.warn('CANNOT find rule for scopeName: ' + pattern.include + ', I am: ', repository['$base'].name);
						}
					} else if (pattern.include == "$base" || pattern.include == "$self") {
						// Special include also found in `repository`
						patternId = RuleFactory.getCompiledRuleId(repository[pattern.include], helper, repository);
					} else {
						let externalGrammarName: string = null,
							externalGrammarInclude: string = null,
							sharpIndex = pattern.include.indexOf('#');
						if (sharpIndex >= 0) {
							externalGrammarName = pattern.include.substring(0, sharpIndex);
							externalGrammarInclude = pattern.include.substring(sharpIndex + 1);
						} else {
							externalGrammarName = pattern.include;
						}
						// External include
						externalGrammar = helper.getExternalGrammar(externalGrammarName, repository);

						if (externalGrammar) {
							if (externalGrammarInclude) {
								let externalIncludedRule = externalGrammar.repository[externalGrammarInclude];
								if (externalIncludedRule) {
									patternId = RuleFactory.getCompiledRuleId(externalIncludedRule, helper, externalGrammar.repository);
								} else {
									// console.warn('CANNOT find rule for scopeName: ' + pattern.include + ', I am: ', repository['$base'].name);
								}
							} else {
								patternId = RuleFactory.getCompiledRuleId(externalGrammar.repository.$self, helper, externalGrammar.repository);
							}
						} else {
							// console.warn('CANNOT find grammar for scopeName: ' + pattern.include + ', I am: ', repository['$base'].name);
						}

					}
				} else {
					patternId = RuleFactory.getCompiledRuleId(pattern, helper, repository);
				}

				if (patternId !== -1) {
					rule = helper.getRule(patternId);

					skipRule = false;

					if (rule instanceof IncludeOnlyRule) {
						IncludeOnlyRule ior = (IncludeOnlyRule) rule;
						if (ior.hasMissingPatterns && ior.patterns.length == 0) {
							skipRule = true;
						}
					} else if (rule instanceof BeginEndRule) {
						BeginEndRule br = (BeginEndRule) rule;
						if (br.hasMissingPatterns && br.patterns.length == 0) {
							skipRule = true;
						}
					}

					if (skipRule) {
						// console.log('REMOVING RULE ENTIRELY DUE TO EMPTY PATTERNS THAT ARE MISSING');
						continue;
					}

					r.add(patternId);
				}
			}
		}

		return new ICompilePatternsResult(
			r,
			((patterns == null && r.size() == 0) || (patterns.length != r.size()))
			/*((patterns != null ? patterns.length : 0) !== r.length)*/
			
		);
	}

}
