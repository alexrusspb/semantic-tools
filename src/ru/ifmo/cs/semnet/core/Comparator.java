package ru.ifmo.cs.semnet.core;

import java.util.List;

/**
 * Используется операциями выборки и поиска для сравнения 
 * объектов  параметров. Разные реализации  позволяют на
 * лету менять  алгоритм  сравления объектов  (в частном 
 * случае текста).  Это упрощает  разработку  механизмов
 * выборки и неточного поиска.
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public interface Comparator {
	
	/**
	 * Метод сравнения. Вызывается в различных Selector- и 
	 * Finder-объектах. Т.к. в параметрах узла одному ключу
	 * может соответствовать несколько значений, а в перечне
	 * условий выборки только один объект, то по этой причине
	 * данный метод принимает разного вида аргументы.
	 * 
	 * @param network параметры, пришедшие от сети (узла сети)
	 * @param selectorKey образец условия выборки
	 * @return
	 */
	boolean compare(List<Object> network, Object selectorKey);
	
}
