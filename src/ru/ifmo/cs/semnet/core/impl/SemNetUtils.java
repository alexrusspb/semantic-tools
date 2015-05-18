package ru.ifmo.cs.semnet.core.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
	public static final String VIEW_NAME_PARAMETER = "display";
	
	public static final String USE_AREA_NAME_PARAMETER = "concept-area";
	
	/* SPECIAL CONSTANTS */
	public static final Long ROOT_NODE_ID = 0L;

	
	/* NODE LOCALIZATION */
	public static final Locale RUSSIA = new Locale("ru");
	
	
	/* UTILS */
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
}
