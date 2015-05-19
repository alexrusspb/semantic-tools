package ru.ifmo.cs.semnet.core.select;

import java.util.List;

import ru.ifmo.cs.semnet.core.Comparator;

/**
 * Компаратор, используемый при сравнении значений
 * параметров во времы операции выборки из сети
 * 
 * @author Pismak Alexey
 * @lastUpdate 19 мая 2015 г.
 */
public class SelectComparator implements Comparator {

	@Override
	public boolean compare(List<Object> networkKey, Object selectorKey) {
		try {
			for(Object o : networkKey) {
				if(((String) o).equalsIgnoreCase((String)selectorKey)) {
					return true;
				}
			}
			return false;
		} catch (ClassCastException cce) {
			return networkKey.equals(selectorKey);
		}
	}
}
