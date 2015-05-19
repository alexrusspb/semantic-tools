package ru.ifmo.cs.semnet.importer;

import ru.ifmo.cs.semnet.core.Node;

public interface ImportManager<T extends ImportPackage, R extends Node> {

	void registerDriver(ImportDriver<T> driver);
	
	void runInBackgroundMode();
	
}
