package org.eclipse.textmate4e.core.internal.matcher;

public interface IMatcher<T> {
	
	boolean match(T matcherInput);
	
}
