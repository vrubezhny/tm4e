package org.eclipse.tm4e.core.model;

public interface ITokenizationSupport {

	TMState getInitialState();

	LineTokens tokenize(String line, TMState state);
	
	// add offsetDelta to each of the returned indices
	// stop tokenizing at absolute value stopAtOffset (i.e. stream.pos() +
	// offsetDelta > stopAtOffset)
	LineTokens tokenize(String line, TMState state, Integer offsetDelta, Integer stopAtOffset);

}
