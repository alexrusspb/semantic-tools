package ru.ifmo.cs.semnet.core;

public enum NodeLink {
	
	/**
	 * Ссылка на родительский узел
	 */
	PARENT,
	
	/**
	 * Ссылка на дочерний узел
	 */
	CHILD,
	
	/**
	 * Ссылка на узел-синоним
	 */
	SYNONYM,
	
	/**
	 * Ссылка на узел-антоним
	 */
	ANTONYM,
	
	/**
	 * Ссылка на смежное понятие
	 */
	ASSOCIATED_CONCEPT;
	
}
