package ru.ifmo.cs.semnet.core.select;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.ifmo.cs.semnet.core.Comparator;
import ru.ifmo.cs.semnet.core.FindAlgorithm;
import ru.ifmo.cs.semnet.core.Finder;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;

/**
 * Объект простого неточного поиска по семантической сети
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class SimpleFinder implements Finder {
	
	/* "поисковик" ищет только среди текстовых параметров */
	private Map<String, String> parameters;
	
	private Comparator comparator;
	
	private Locale locale;
	
	/**
	 * Инициализация компаратора "поисковика" реализацией
	 * с алгоритмом сравнения N-Gramm
	 */
	public SimpleFinder() {
		setComparator(FindAlgorithm.NGRAMM);
	}
	
	/**
	 * Добавление параметра поиска
	 * 
	 * @param name название параметра
	 * @param value значение параметра
	 */
	public void addParam(String name, String value) {
		if(name != null && value != null) {
			if(parameters == null) {
				parameters = new HashMap<>();
			}
			parameters.put(name.toUpperCase(), value);
		}
	}
	
	/**
	 * @param locale по какой локали ограничить поиск в параметрах узлов
	 */
	public void setFindLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public <T extends Node> boolean checkNode(T node) {
		if(parameters == null) {
			return true;
		}
		Map<String, List<Object>> arg = SemNetUtils.convertParameters(locale, node);
		if(arg == null) {
			return false;
		}
		for(String key : parameters.keySet()) {
			// если заданный параметр в узле есть
			if(SemNetUtils.containsWithIgnoreCase(arg.keySet(), key)) {
				// если среди значений по данному ключу 
				// есть такое как в наборе ограничителей
				if(comparator.compare(arg.get(key), parameters.get(key))) {
					// то переходим к следующему параметру
					continue;
				}
				// если значение не совпало
				return false;
			}
			return false;
		}
		// если параметры перебрали так и не напоровшись на return
		return true;
	}

	@Override
	public void setComparator(Comparator cmp) {
		if(cmp != null) {
			comparator = cmp;
		}
	}

}
