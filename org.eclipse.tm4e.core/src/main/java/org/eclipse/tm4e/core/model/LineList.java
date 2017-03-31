package org.eclipse.tm4e.core.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

class LineList implements IModelLines {
	
	private final List<ModelLine> list = Collections.synchronizedList(new ArrayList<>());
	private Function<Integer, String> lineToTextResolver;

	public LineList(Function<Integer, String> lineToTextResolver) {
		this.lineToTextResolver = lineToTextResolver;
	}
	
	@Override
	public void addLine(int line) {
		try {
			this.list.add(line, new ModelLine(this.lineToTextResolver.apply(line)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void removeLine(int line) {
		this.list.remove(line);
	}

	@Override
	public void updateLine(int line) {
		try {
			this.list.get(line).text = this.lineToTextResolver.apply(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ModelLine get(int index) {
		return this.list.get(index);
	}

	@Override
	public int getSize() {
		return this.list.size();
	}

	public void forEach(Consumer<ModelLine> consumer) {
		this.list.forEach(consumer);
	}
}