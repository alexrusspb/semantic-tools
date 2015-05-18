package ru.ifmo.cs.semnet.importer;

@FunctionalInterface
public interface ImportListener {
	
	void doImport(ImportPackage pack);
	
}
