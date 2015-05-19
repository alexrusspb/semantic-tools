package ru.ifmo.cs.semnet.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import ru.ifmo.cs.semnet.core.Node;

/**
 * Вспомогательные константы и методы для 
 * оперирования узлами и различными параметрами
 * 
 * @author Pismak Alexey
 * @lastUpdate 17 мая 2015 г.
 */
public class SemNetUtils {

	/* NODE PARAMETERS */
	public static final String VIEW_NAME_PARAMETER = "DISPLAY";
	
	public static final String USE_AREA_NAME_PARAMETER = "CONCEPT-AREA";
	
	/* SPECIAL CONSTANTS */
	public static final Long ROOT_NODE_ID = 0L;

	
	/* NODE LOCALIZATION */
	public static final Locale RUSSIA = new Locale("ru");
	
	
	/* UTILS */
	
	/**
	 * Формирование "правильного"  набора параметров узла.
	 * Т.к. каждый узел можеть иметь несколько одинаковых 
	 * параметров, то при слиянии их в один набор, дубликат
	 * на сохраняется. Этом метот формирует набор, где 
	 * каждому параметру назначается список его значений
	 * 
	 * @param l для какой локали использовать параметры (может быть null)
	 * @param n для какого узла получить этот список
	 * @return набор параметров
	 */
	public static Map<String, List<Object>> convertParameters(Locale l, Node n) {
		try {
			Map<String, List<Object>> arg = null;
			if(l == null) {
				arg = n.getAllParameters();
			} else {
				// конвертируем Map<String, Object> в Map<String, List<Object>>
				arg = new HashMap<>();
				Map<String, Object> map = n.getAllLocalizedParameters(l);
				for(String s : map.keySet()) {
					List<Object> value = new ArrayList<>();
					value.add(map.get(s));
					arg.put(s, value);
				}
			}
			return arg;
		} catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Проверка на набора строк на содержание в нем строки без учета регистра
	 * 
	 * @param set проверяемый набор
	 * @param key какое слово искать в наборе
	 * @return <code>true</code> если слово найдено, иначе <code>false</code>
	 */
	public static boolean containsWithIgnoreCase(Set<String> set, String key) {
		return !set.stream().filter(s -> s.equalsIgnoreCase(key))
				.collect(Collectors.toList()).isEmpty();
	}
}
