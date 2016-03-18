package fr.opensagres.language.textmate.rule;

public class CaptureRule extends Rule {

	public Integer retokenizeCapturedWithRuleId;

	public CaptureRule(int id, String name, String contentName, Integer retokenizeCapturedWithRuleId) {
		super(id, name, contentName);
		this.retokenizeCapturedWithRuleId = retokenizeCapturedWithRuleId;
	}

}
