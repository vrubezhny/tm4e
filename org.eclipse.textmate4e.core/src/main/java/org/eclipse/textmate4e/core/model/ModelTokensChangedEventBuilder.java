package org.eclipse.textmate4e.core.model;

import java.util.ArrayList;
import java.util.List;

class ModelTokensChangedEventBuilder {

	private final ITMModel model;
	private final List<Range> ranges;

	public ModelTokensChangedEventBuilder(ITMModel model) {
		this.model = model;
		this.ranges = new ArrayList<>();
	}

	public void registerChangedTokens(int lineNumber) {
		int rangesLength = ranges.size();
		Range previousRange = rangesLength > 0 ? ranges.get(rangesLength - 1) : null;

		if (previousRange != null && previousRange.toLineNumber == lineNumber - 1) {
			// extend previous range
			previousRange.toLineNumber++;
		} else {
			// insert new range
			ranges.add(new Range(lineNumber, lineNumber));
		}
	}

	public ModelTokensChangedEvent build() {
		if (this.ranges.size() == 0) {
			return null;
		}
		return new ModelTokensChangedEvent(ranges, model);
	}
}
