package ru.ifmo.cs.semnet.core;

/**
 * Поведение объекта, обрабатывающего 
 * очередной найденный элемент при выборке
 * 
 * @author alex
 */
@FunctionalInterface
public interface SelectResult {
	
	/**
	 * 
	 * @param searchResult
	 */
	void result(Node searchResult);
	
}
