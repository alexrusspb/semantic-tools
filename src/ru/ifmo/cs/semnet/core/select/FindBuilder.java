package ru.ifmo.cs.semnet.core.select;

import java.util.Locale;

import ru.ifmo.cs.semnet.core.Finder;

/**
 * Строитель объектов поиска
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class FindBuilder {
	
	private SimpleFinder finder;
	
	private FindBuilder() {
		finder = new SimpleFinder();
	}
	
	/**
	 * Задает основной критерий поиска
	 * 
	 * @param where где искать (имя параметра)
	 * @param what что искать (содержимое)
	 * @return {@link FindBuilder}
	 */
	public static FindBuilder find(String where, String what) {
		return new FindBuilder().and(where, what);
	}
	
	/**
	 * Добавление условия поиска
	 * 
	 * @param where где искать (имя параметра)
	 * @param what что искать (содержимое)
	 * @return {@link FindBuilder}
	 */
	public FindBuilder and(String where, String what) {
		finder.addParam(where, what);
		return this;
	}
	
	/**
	 * Построение "поисковика" с заданной локалью
	 * 
	 * @param byLang может быть null
	 * @return {@link Finder}
	 */
	public Finder build(Locale byLang) {
		return finder;
	}
}
