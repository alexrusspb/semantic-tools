package ru.ifmo.cs.semnet.core;

@FunctionalInterface
public interface Selector {

	<T extends Node> boolean checkNode(T node);
	
}
