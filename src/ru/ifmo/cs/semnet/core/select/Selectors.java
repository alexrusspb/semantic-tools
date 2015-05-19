package ru.ifmo.cs.semnet.core.select;

import java.util.Locale;

import ru.ifmo.cs.semnet.core.Finder;
import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;

/**
 * Фабрика объектов выборки и поиска из сети
 * 
 * @author Pismak Alexey
 * @lastUpdate 19 мая 2015 г.
 */
public class Selectors {
	
	/**
	 * Инициализация экзепляра объекта простой выборки по представлению узла
	 * 
	 * @param view представление узла
	 * @return экземпляр объекта выборки
	 */
	public static Selector cteareSelectByView(String view) {
		return SelectBuilder.select().param(SemNetUtils.VIEW_NAME_PARAMETER, view).build();
	}
	
	/**
	 * Инициализация экзепляра объекта локализованной выборки по представлению узла
	 * 
	 * @param view представление узла
	 * @param locale для какой локали
	 * @return экземпляр объекта выборки
	 */
	public static Selector cteareSelectByLocalizeView(String view, Locale locale) {
		return SelectBuilder.select().param(SemNetUtils.VIEW_NAME_PARAMETER, view)
				.by(locale).build();
	}
	
	/**
	 * Форирует экземпляр объекта выборки всех узлов в сети
	 * 
	 * @return экземпляр объекта выборки
	 */
	public static Selector createSelectAll() {
		return SelectBuilder.select().build();
	}
	
	/**
	 * Создание объекта неточного поиска по представлению узла
	 * 
	 * @param view представление узла
	 * @return экземпляр объекта выборки
	 */
	public static Finder createFindByView(String view) {
		return FindBuilder.find(SemNetUtils.VIEW_NAME_PARAMETER, view).build(null);
	}
	
	/**
	 * Создание объекта неточного поиска по локализоанному представлению узла
	 * 
	 * @param view представление узла
	 * @param locale для какой локали ограничен поиск
	 * @return экземпляр объекта выборки
	 */
	public static Finder createFindByLocalizeView(String view, Locale locale) {
		return FindBuilder.find(SemNetUtils.VIEW_NAME_PARAMETER, view).build(locale);
	}
}
