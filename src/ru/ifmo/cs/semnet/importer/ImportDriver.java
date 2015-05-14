package ru.ifmo.cs.semnet.importer;

import java.io.ObjectInputStream;
import java.io.Serializable;

import ru.ifmo.cs.semnet.core.Node;

public interface ImportDriver<T extends Node> extends Serializable {
	
	boolean hasUpdate();
	
	T getNextNodeItem();
	
	long getCountNewEntries();
	
	ObjectInputStream getNodesStream();
	
}
