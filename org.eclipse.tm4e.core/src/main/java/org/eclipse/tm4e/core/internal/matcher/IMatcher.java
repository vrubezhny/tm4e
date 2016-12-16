package org.eclipse.tm4e.core.internal.matcher;

public interface IMatcher<T> {
	
	boolean match(T matcherInput);
	
}
