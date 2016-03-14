package fr.opensagres.language.textmate.types;

public interface IRawRepository {

	// IRawRule getRule(String name);
	
	IRawRule getProp(String name);
	
	IRawRule getBase();

	IRawRule getSelf();

}
