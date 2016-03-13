package fr.opensagres.language.textmate.rule;

public class CaptureRule extends Rule {

	public int retokenizeCapturedWithRuleId;

	public CaptureRule(int id, String name, String contentName, int retokenizeCapturedWithRuleId) {
		super(id, name, contentName);
		this.retokenizeCapturedWithRuleId = retokenizeCapturedWithRuleId;
	}

}
