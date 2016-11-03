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

	public int getRuleId() {
		return ruleId;
	}

	public String getEndRule() {
		return endRule;
	}

	public void setEndRule(String endRule) {
		this.endRule = endRule;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StackElement)) {
			return false;
		}
		StackElement other = (StackElement) obj;
		if (!this._shallowEquals(other)) {
			return false;
		}
		// TODO : parent
		// if (!this._parent && !other._parent) {
		// return true;
		// }
		// if (!this._parent || !other._parent) {
		// return false;
		// }
		// return this._parent.equals(other._parent);
		return true;
	}

	private boolean _shallowEquals(StackElement other) {
		return (this.ruleId == other.ruleId && this.endRule == other.endRule
				&& isEquals(this.scopeName, other.scopeName) && isEquals(this.contentName, other.contentName));
	}

	private boolean isEquals(String s1, String s2) {
		if (s1 == null) {
			return s2 == null;
		}
		return s1.equals(s2);
	}
}
