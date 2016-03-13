package fr.opensagres.language.textmate.rule;

import java.util.Collection;

import textmate.types.IRawCaptures;
import textmate.types.IRawRepository;
import textmate.types.IRawRule;

public class RuleFactory {

	public static int getCompiledRuleId(final IRawRule desc, final IRuleFactoryHelper helper, final IRawRepository repository) {
		if (desc.getId() == null) {

			helper.registerRule(new IRuleFactory() {

				@Override
				public Rule create(int id) {
					desc.setId(id);

					if (desc.getMatch() != null) {
						return new MatchRule(
							desc.getId(),
							desc.getName(),
							desc.getMatch(),
							RuleFactory._compileCaptures(desc.getCaptures(), helper, repository)
						);
					}
					
					if (desc.getBegin() == null) {
						/*if (desc.getRepository() != null) {
							repository = null; //mergeObjects({}, repository, desc.repository);
						}*/
						return new IncludeOnlyRule(
							desc.getId(),
							desc.getName(),
							desc.getContentName(),
							RuleFactory._compilePatterns(desc.patterns, helper, repository)
						);
					}

					return new BeginEndRule(
						desc.getId(),
						desc.getName(),
						desc.getContentName(),
						desc.getBegin(), RuleFactory._compileCaptures(desc.beginCaptures || desc.captures, helper, repository),
						desc.end, RuleFactory._compileCaptures(desc.endCaptures || desc.captures, helper, repository),
						desc.applyEndPatternLast,
						RuleFactory._compilePatterns(desc.patterns, helper, repository)
					);
				}
			});
		}

		return desc.getId();
	}
	
	private static Collection<CaptureRule> _compileCaptures(IRawCaptures captures, IRuleFactoryHelper helper, IRawRepository repository) {
		let r: CaptureRule[] = [],
			numericCaptureId: number,
			maximumCaptureId: number,
			i: number,
			captureId: string;

		if (captures) {
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
				let retokenizeCapturedWithRuleId = 0;
				if (captures[captureId].patterns) {
					retokenizeCapturedWithRuleId = RuleFactory.getCompiledRuleId(captures[captureId], helper, repository);
				}
				r[numericCaptureId] = RuleFactory.createCaptureRule(helper, captures[captureId].name, captures[captureId].contentName, retokenizeCapturedWithRuleId);
			}
		}

		return r;
	}
}
