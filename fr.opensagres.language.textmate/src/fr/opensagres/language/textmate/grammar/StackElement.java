package fr.opensagres.language.textmate.grammar;

public class StackElement {

	private int ruleId;
	private int enterPos;
	private String endRule;
	private String scopeName;
	private String contentName;

	public StackElement(int ruleId, int enterPos, String endRule, String scopeName, String contentName) {
		this.ruleId = ruleId;
		this.enterPos = enterPos;
		this.endRule = endRule;
		this.scopeName = scopeName;
		this.contentName = contentName;
	}

	public StackElement clone() {
		return new StackElement(this.ruleId, this.enterPos, this.endRule, this.scopeName, this.contentName);
	}

	public boolean matches(String scopeName) {
		if (this.scopeName == null) {
			return false;
		}
		if (this.scopeName.equals(scopeName)) {
			return true;
		}
		int len = scopeName.length();
		return this.scopeName.length() > len && this.scopeName.substring(0, len).equals(scopeName)
				&& this.scopeName.charAt(len) == '.';
	}

	public void setEnterPos(int enterPos) {
		this.enterPos = enterPos;
	}

	public int getEnterPos() {
		return enterPos;
	}
	
	public String getScopeName() {
		return scopeName;
	}
	
	public String getContentName() {
		return contentName;
	}
	
	
}
