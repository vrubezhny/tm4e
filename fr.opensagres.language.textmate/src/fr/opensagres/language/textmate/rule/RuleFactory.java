package fr.opensagres.language.textmate.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.opensagres.language.textmate.grammar.parser.Raw;
import fr.opensagres.language.textmate.types.IRawCaptures;
import fr.opensagres.language.textmate.types.IRawGrammar;
import fr.opensagres.language.textmate.types.IRawRepository;
import fr.opensagres.language.textmate.types.IRawRule;

public class RuleFactory {

	public static CaptureRule createCaptureRule(IRuleFactoryHelper helper, final String name, final String contentName,
			final Integer retokenizeCapturedWithRuleId) {
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
						IRawRepository r = repository;
						if (desc.getRepository() != null) {
							r = mergeObjects(repository, desc.getRepository());
						}
						return new IncludeOnlyRule(desc.getId(), desc.getName(), desc.getContentName(),
								RuleFactory._compilePatterns(desc.getPatterns(), helper, r));
					}

					return new BeginEndRule(desc.getId(), desc.getName(), desc.getContentName(), desc.getBegin(),
							RuleFactory._compileCaptures(
									desc.getBeginCaptures() != null ? desc.getBeginCaptures() : desc.getCaptures(),
									helper, repository),
							desc.getEnd(),
							RuleFactory._compileCaptures(
									desc.getEndCaptures() != null ? desc.getEndCaptures() : desc.getCaptures(), helper,
									repository),
							desc.isApplyEndPatternLast(),
							RuleFactory._compilePatterns(desc.getPatterns(), helper, repository));
				}

				private IRawRepository mergeObjects(IRawRepository... sources) {
					Raw target = new Raw();
					for (IRawRepository source : sources) {
						Set<Entry<String, Object>> entries = ((Map<String, Object>) source).entrySet();
						for (Entry<String, Object> entry : entries) {
							target.put(entry.getKey(), entry.getValue());
						}
					}
					return target;
				}

			});
		}

		return desc.getId();
	}

	private static List<CaptureRule> _compileCaptures(IRawCaptures captures, IRuleFactoryHelper helper,
			IRawRepository repository) {
		List<CaptureRule> r = new ArrayList<CaptureRule>();
		int numericCaptureId;
		int maximumCaptureId;
		int i;

		if (captures != null) {
			// Find the maximum capture id
			maximumCaptureId = 0;
			for (String captureId : captures) {
				numericCaptureId = parseInt(captureId, 10);
				if (numericCaptureId > maximumCaptureId) {
					maximumCaptureId = numericCaptureId;
				}
			}

			// Initialize result
			for (i = 0; i <= maximumCaptureId; i++) {
				r.add(null);
			}

			// Fill out result
			for (String captureId : captures) {
				numericCaptureId = parseInt(captureId, 10);
				Integer retokenizeCapturedWithRuleId = null;
				IRawRule rule = captures.getCapture(captureId);
				if (rule.getPatterns() != null) {
					retokenizeCapturedWithRuleId = RuleFactory.getCompiledRuleId(captures.getCapture(captureId), helper,
							repository);
				}
				r.set(numericCaptureId, RuleFactory.createCaptureRule(helper, rule.getName(), rule.getContentName(),
						retokenizeCapturedWithRuleId));
			}
		}

		return r;
	}

	private static int parseInt(String string, int base) {
		try {
			return Integer.parseInt(string, base);
		} catch (Throwable e) {
			return 0;
		}
	}

	private static ICompilePatternsResult _compilePatterns(Collection<IRawRule> patterns, IRuleFactoryHelper helper,
			IRawRepository repository) {
		Collection<Integer> r = new ArrayList<Integer>();
		int i;
		int len;
		int patternId;
		IRawGrammar externalGrammar;
		Rule rule;
		boolean skipRule;

		if (patterns != null) {
			for (IRawRule pattern : patterns) {
				patternId = -1;

				if (pattern.getInclude() != null) {
					if (pattern.getInclude().charAt(0) == '#') {
						// Local include found in `repository`
						IRawRule localIncludedRule = repository.getProp(pattern.getInclude().substring(1));
						if (localIncludedRule != null) {
							patternId = RuleFactory.getCompiledRuleId(localIncludedRule, helper, repository);
						} else {
							// console.warn('CANNOT find rule for scopeName: ' +
							// pattern.include + ', I am: ',
							// repository['$base'].name);
						}
					} else if (pattern.getInclude().equals("$base") || pattern.getInclude().equals("$self")) {
						// Special include also found in `repository`
						patternId = RuleFactory.getCompiledRuleId(repository.getProp(pattern.getInclude()), helper,
								repository);
					} else {
						String externalGrammarName = null, externalGrammarInclude = null;
						int sharpIndex = pattern.getInclude().indexOf('#');
						if (sharpIndex >= 0) {
							externalGrammarName = pattern.getInclude().substring(0, sharpIndex);
							externalGrammarInclude = pattern.getInclude().substring(sharpIndex + 1);
						} else {
							externalGrammarName = pattern.getInclude();
						}
						// External include
						externalGrammar = helper.getExternalGrammar(externalGrammarName, repository);

						if (externalGrammar != null) {
							if (externalGrammarInclude != null) {
								IRawRule externalIncludedRule = externalGrammar.getRepository()
										.getProp(externalGrammarInclude);
								if (externalIncludedRule != null) {
									patternId = RuleFactory.getCompiledRuleId(externalIncludedRule, helper,
											externalGrammar.getRepository());
								} else {
									// console.warn('CANNOT find rule for
									// scopeName: ' + pattern.include + ', I am:
									// ', repository['$base'].name);
								}
							} else {
								patternId = RuleFactory.getCompiledRuleId(externalGrammar.getRepository().getSelf(),
										helper, externalGrammar.getRepository());
							}
						} else {
							// console.warn('CANNOT find grammar for scopeName:
							// ' + pattern.include + ', I am: ',
							// repository['$base'].name);
						}

					}
				} else {
					patternId = RuleFactory.getCompiledRuleId(pattern, helper, repository);
				}

				if (patternId != -1) {
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
						// console.log('REMOVING RULE ENTIRELY DUE TO EMPTY
						// PATTERNS THAT ARE MISSING');
						continue;
					}

					r.add(patternId);
				}
			}
		}

		return new ICompilePatternsResult(r, ((patterns != null ? patterns.size() : 0) != r.size()));
	}

}
