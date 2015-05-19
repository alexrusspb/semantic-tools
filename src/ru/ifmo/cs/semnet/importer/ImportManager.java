package ru.ifmo.cs.semnet.importer;

import java.io.Closeable;

import ru.ifmo.cs.semnet.core.Node;

public interface ImportManager<T extends ImportPackage, R extends Node> extends Closeable {

	void registerDriver(ImportDriver<T> driver);
	
	void runInBackgroundMode();
	
}
