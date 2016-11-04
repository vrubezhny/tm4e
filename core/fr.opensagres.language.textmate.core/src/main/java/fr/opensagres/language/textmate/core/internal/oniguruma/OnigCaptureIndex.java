package fr.opensagres.language.textmate.core.internal.oniguruma;

public class OnigCaptureIndex implements IOnigCaptureIndex {

	private final int index;
	private final int start;
	private final int end;

	public OnigCaptureIndex(int index, int start, int end) {
		this.index = index;
		this.start = start >= 0 ? start : 0;
		this.end = end >= 0 ? end : 0;
	}

	@Override
	public int getIndex() {
		return index;
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public int getEnd() {
		return end;
	}

	@Override
	public int getLength() {
		return end - start;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{\"index\": ");
		result.append(getIndex());
		result.append(", \"start\": ");
		result.append(getStart());
		result.append(", \"end\": ");
		result.append(getEnd());
		result.append(", \"length\": ");
		result.append(getLength());
		result.append("}");
		return result.toString();
	}
}
