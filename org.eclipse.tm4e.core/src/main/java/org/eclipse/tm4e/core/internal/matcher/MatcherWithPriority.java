package org.eclipse.tm4e.core.internal.matcher;

public class MatcherWithPriority<T> {

	public final IMatcher<T> matcher;

	public final int priority;

	/**
	 * @param matcher
	 * @param priority
	 */
	public MatcherWithPriority(IMatcher<T> matcher, int priority) {
		this.matcher = matcher;
		this.priority = priority;
	}

}
