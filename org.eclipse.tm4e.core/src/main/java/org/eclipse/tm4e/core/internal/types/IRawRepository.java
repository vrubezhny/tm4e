package org.eclipse.tm4e.core.internal.types;

public interface IRawRepository {

	// IRawRule getRule(String name);

	IRawRule getProp(String name);

	IRawRule getBase();

	IRawRule getSelf();

	void setSelf(IRawRule raw);

	void setBase(IRawRule base);

}
