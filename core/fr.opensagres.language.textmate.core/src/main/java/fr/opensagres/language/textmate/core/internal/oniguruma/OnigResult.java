package fr.opensagres.language.textmate.core.internal.oniguruma;

import java.util.ArrayList;
import java.util.List;

import org.joni.Region;

public class OnigResult implements IOnigNextMatchResult {

	private int index;
	private Region region;
	private List<IOnigCaptureIndex> captureIndices;

	public OnigResult(int index, Region region) {
		this.captureIndices = new ArrayList<IOnigCaptureIndex>();
		this.update(index, region);
	}

	@Override
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void update(int index, Region region) {
		this.index = index;
		this.region = region;
		this.captureIndices.clear();
	}

	@Override
	public IOnigCaptureIndex[] getCaptureIndices() {
		if (region.beg.length != captureIndices.size()) {
			captureIndices.clear();
			int captureStart = -1, captureEnd = -1;
			for (int i = 0; i < region.beg.length; i++) {
				captureStart = region.beg[i];
				captureEnd = region.end[i];
				captureIndices.add(new OnigCaptureIndex(i, captureStart, captureEnd));
			}
		}
		return captureIndices.toArray(new IOnigCaptureIndex[0]);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{\n");
		result.append("  \"index\": ");
		result.append(getIndex());
		result.append(",\n");
		result.append("  \"captureIndices\": [\n");
		int i = 0;
		for (IOnigCaptureIndex captureIndex : getCaptureIndices()) {
			if (i > 0) {
				result.append(",\n");
			}
			result.append("    ");
			result.append(captureIndex);
			i++;
		}
		result.append("\n");
		result.append("  ]\n");
		result.append("}");
		return result.toString();
	}

	public int LocationAt(int index) {
		if (region.beg.length > 0) {
			return region.beg[0] + index;
		}
		return 0;
	}

	public int count() {
		return region.beg.length;
	}

}
