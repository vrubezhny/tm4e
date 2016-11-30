package org.eclipse.textmate4e.core.grammar;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.textmate4e.core.internal.rule.IRuleRegistry;
import org.eclipse.textmate4e.core.internal.rule.Rule;

/**
 * 
 * @see https://github.com/Microsoft/vscode-textmate/blob/master/src/grammar.ts
 *
 */
public class StackElement {

	public final StackElement _parent;
	private final int ruleId;
	private int enterPos;
	private String endRule;
	private final String scopeName;
	private String contentName;

	public StackElement(StackElement parent, int ruleId, int enterPos, String endRule, String scopeName,
			String contentName) {
		this._parent = parent;
		this.ruleId = ruleId;
		this.enterPos = enterPos;
		this.endRule = endRule;
		this.scopeName = scopeName;
		this.contentName = contentName;
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
		if (this._parent == null && other._parent == null) {
			return true;
		}
		if (this._parent == null || other._parent == null) {
			return false;
		}
		return this._parent.equals(other._parent);
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

	public void reset() {
		this.enterPos = -1;
		if (this._parent != null) {
			this._parent.reset();
		}
	}

	public StackElement pop() {
		return this._parent;
	}

	public StackElement safePop() {
		if (this._parent != null) {
			return this._parent;
		}
		return this;
	}

	public StackElement pushElement(StackElement what) {
		return this.push(what.ruleId, what.enterPos, what.endRule, what.scopeName, what.contentName);
	}

	public StackElement push(int ruleId, int enterPos, String endRule, String scopeName, String contentName) {
		return new StackElement(this, ruleId, enterPos, endRule, scopeName, contentName);
	}

	public StackElement withContentName(String contentName) {
		if (isEquals(this.contentName, contentName)) {
			return this;
		}
		return new StackElement(this._parent, this.ruleId, this.enterPos, this.endRule, this.scopeName, contentName);
	}

	public StackElement withEndRule(String endRule) {
		if (isEquals(this.endRule, endRule)) {
			return this;
		}
		return new StackElement(this._parent, this.ruleId, this.enterPos, endRule, this.scopeName, this.contentName);
	}

	private int _writeScopes(List<String> scopes, int outIndex) {
		if (this._parent != null) {
			outIndex = this._parent._writeScopes(scopes, outIndex);
		}

		if (this.scopeName != null) {
			// scopes[outIndex++] = this.scopeName;
			outIndex++;
			scopes.add(this.scopeName);
		}

		if (this.contentName != null) {
			outIndex++;
			// scopes[outIndex++] = this.contentName;
			scopes.add(this.contentName);
		}

		return outIndex;
	}

	/**
	 * Token scopes
	 */
	public List<String> generateScopes() {
		List<String> result = new ArrayList<>();
		this._writeScopes(result, 0);
		return result;
	}

	public boolean hasSameRuleAs(StackElement other) {
		return this.ruleId == other.ruleId;
	}

	public Rule getRule(IRuleRegistry grammar) {
		return grammar.getRule(this.ruleId);
	}
}
