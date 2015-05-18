package ru.ifmo.cs.semnet.core.impl.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.TypeLink;
import ru.ifmo.cs.semnet.core.exeption.LangNotSupportedException;

/**
 * Простой объект выборки. Содержит критерии поиска и позволяет 
 * проверить любой необходимый узел по заданным критериям. 
 * Описать  работу  селектора  можно  следующим  образом: 
 * 
 * пусть условия выборки представлены так
 * 
 * SELECT WITH ( 
 * 			ID (1, 2, 3), 
 * 			LINK (2 -> CHILD, 3 -> PARENT), 
 * 			PARAM ("display" -> "стол") BY RU
 * 				);
 * 
 * В этом случае будет сформировал селектор со следующими критериями:
 * 	1. узел должен иметь один из указанных ID
 * 	2. узел должен быть связан с узлами 2 и 3, 
 * 		как с дочерним и родительским соответственно
 * 	3. узел должен иметь параметр "display" со значением "стол"
 * 		в локализованном параметре на русском языке
 * 
 * Любой из параметров можно опустить. Это дает возможность
 * гибко формировать запросы на выборку из сети. Примеры:
 * 
 * 	Выбрать синонимы понятия "воздух" (id которого известен - 123456):
 * SELECT WIDTH (
 * 			LINK (123456 -> SYNONYM),
 * 			PARAM ("display" -> "воздух")
 * 				) BY RU;
 * 
 * 	Выбрать все понятия, из области физики:
 * SELECT WIDTH ( PARAM ("concept-area" -> "phisics") );
 * 
 * Программная реализация синтаксиса {@link=SelectBuilder}
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class SimpleSelector implements Selector {
	
	/* узлы с какими ID пропускать при фильтрации */
	private ArrayList<Long> protectByIds;
	
	/* какие ссылки должен иметь узел, чтобы пройти фильтрацию */
	private Map<Long, TypeLink> protectByLinks;
	
	/* какие параметры должен имет узел, чтобы пройти фильтрацию */
	private Map<String, Object> protectByParams;
	
	/* какой локалью ограничить просмотр параметров (либо брать все параметры) */
	private Locale loc;
	
	@Override
	public <T extends Node> boolean checkNode(T node) {
		try {
			Map<String, List<Object>> arg = null;
			if(loc == null) {
				arg = node.getAllParameters();
			} else {
				// конвертируем Map<String, Object> в Map<String, List<Object>>
				arg = new HashMap<>();
				Map<String, Object> map = node.getAllLocalizedParameters(loc);
				for(String s : map.keySet()) {
					List<Object> value = new ArrayList<>();
					value.add(map.get(s));
					arg.put(s, value);
				}
			}
			return checkIds(node.getId()) && checkLinks(node.getLinks()) 
				&& checkParams(arg);
		} catch (LangNotSupportedException e) {
			// если задана локаль, которую узел не поддерживает
			return false;
		}
	}

	/**
	 * Добавить ID к списку разрешенных. При фильтрации, если
	 * ID узла будет одним из добавленых, то фильтрация по ID
	 * будет пройдена успешно
	 * 
	 * @param id идентификатор существующего в сети узла
	 */
	public void addId(long id) {
		if(protectByIds == null) {
			protectByIds = new ArrayList<>();
		}
		if(!protectByIds.contains(id)) {
			protectByIds.add(id);
		}
	}
	
	/**
	 * Добавление обязательной ссылки. При фильтрации, узел
	 * будет пропущен дальше, если в его ссылках найдены все
	 * добавленные этим методом ссылки на указанные узлы.
	 * 
	 * @param id идентификатор узла на который надо ссылаться
	 * @param link тип необходимой связи
	 */
	public void addLink(long id, TypeLink link) {
		if(protectByLinks == null) {
			protectByLinks = new HashMap<>();
		}
		if(!protectByLinks.containsKey(id)) {
			protectByLinks.put(id, link);
		}
	}
	
	/**
	 * Добавление обязательного параметра. При фильтрации,
	 * если узел содержит все заданные параметры, то он
	 * будет пропущен на следующий этап фильтрации.
	 * 
	 * @param name
	 * @param value
	 */
	public void addParam(String name, Object value) {
		if(protectByParams == null) {
			protectByParams = new HashMap<>();
		}
		if(!protectByParams.containsKey(name)) {
			protectByParams.put(name, value);
		}
	}
	
	/**
	 * Задает локаль для ограничения набора перебираемых
	 * параметров, если конечно локаль известна
	 * 
	 * @param locale заданная локаль
	 */
	public void changeLocaleProtect(Locale locale) {
		loc = locale;
	}
	
	protected boolean checkIds(long id) {
		if(protectByIds != null) {
			return protectByIds.contains(id);
		}
		return true;
	}
	
	protected boolean checkParams(Map<String, List<Object>> nodeParams) {
		if(protectByParams == null) {
			return true;
		}
		for(String key : protectByParams.keySet()) {
			// если заданный параметр в узле есть
			if(nodeParams.containsKey(key)) {
				// если среди значений по данному ключу 
				// есть такое как в наборе ограничителей
				if(nodeParams.get(key).contains(protectByParams.get(key))) {
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
	
	protected boolean checkLinks(Map<Long, TypeLink> links) {
		if(protectByLinks == null) {
			return true;
		}
		for(long id : protectByLinks.keySet()) {
			if(links.containsKey(id)) {
				if(links.get(id) .equals(protectByLinks.get(id))) {
					continue;
				}
				return false;
			}
			return false;
		}
		return true;
	}
}
