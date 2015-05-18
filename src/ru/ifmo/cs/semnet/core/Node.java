package ru.ifmo.cs.semnet.core;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ru.ifmo.cs.semnet.core.exeption.LangNotSupportedException;

/**
 * Описание поведения и функциональности узлов семантической сети.
 * Узел в общем случае  представляет собой набор ссылок на другие 
 * узлы,   набор  характеристик  (параметров)  заданного  понятия, 
 * хранение которых обеспечивается для конкретных заданных локалей.
 * Т.е. параметр  с одним  именем  может  характеризовать  узел по
 * разному  для разных  локалей.  Примером такого  параметра может 
 * являться представление  понятия в  текстовом виде.  Для каждого 
 * языка параметр будет имет свое представление. 
 * 
 * @lastUpdate 17 мая 2015 г.
 * @author Pismak Alexey
 */
public interface Node extends Serializable {
	
	/**
	 * @return Уникальный идентификатор узла, полученный при его создании.
	 */
	long getId();
	
	/**
	 * @return возвращает строку, которая определяет представление
	 * 			понятия в текстовом виде на естественном языке для
	 * 			локали по умолчанию. Если представления нет для
	 * 			этой локали, то получим первое найденное.
	 */
	String getView();
	
	/**
	 * Поиск представления для заданной локали
	 * 
	 * @param locale для какой локали будем искать представление
	 * @return возвращает строку, которая определяет представление
	 * 			понятия в текстовом виде на естественном языке
	 * @throws LangNotSupportedException
	 */
	String getView(Locale locale) throws LangNotSupportedException;
	
	/**
	 * Получение списка дочерних узлов понятия
	 * 
	 * @return список узлов сети, являющихся дочерними
	 */
	List<Node> getChilds();
	
	/**
	 * Получение списка узлов, связь с которыми 
	 * имеет заданный аргументом тип
	 * 
	 * @param type тип искомых связей. Если null, то
	 * 			возвращаются все типы связей
	 * @return список узлов с искомыми связями
	 */
	List<Node> getLinks(TypeLink type);
	
	/**
	 * @return все связи данного узла в сети
	 */
	Map<Long, TypeLink> getLinks();
	
	/**
	 * Добавление новой ссылки в узел
	 * 
	 * @param idNode идентификатор узла на который будем ссылаться
	 * @param type тип устанавливаемой с ним связи
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	boolean addLink(long idNode, TypeLink type);
	
	/**
	 * Удаление связи с заданным узлом
	 * 
	 * @param idNode идентификатор заданного узла
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	boolean removeLink(long idNode);
	
	/**
	 * Удаление всех ссылок заданного типа
	 * 
	 * @param type тип ссылок, которые будут удалены
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	boolean removeLinks(TypeLink type);
	
	/**
	 * Удаление всех ссылок, кроме ссылки типа TypeLink.PARENT.
	 */
	void clearLinks();
	
	/**
	 * Вызывается перед отключением узла от сети.
	 * Метод очищает все ссылки на все виды узлов,
	 * а так же удаляет все параметры и локали.
	 */
	void onRemoveNode();
	
	/**
	 * Добавление дочернего узла сети
	 * 
	 * @param idNode идентификатор узла
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	boolean addChild(long idNode);
	
	/**
	 * Удаление дочернего элемента узла
	 * 
	 * @param idChild идентификатор дочернего узла сети
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	boolean removeChild(long idChild);
	
	/**
	 * @return получение ссылки на дочерний узел
	 */
	Node getParentNode();
	
	/**
	 * Смена родительского узла сети. Данным методом можно
	 * переместить поддерево заданного узла в другое место сети
	 * 
	 * @param newParentId
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	boolean changeParent(long newParentId);
	
	/**
	 * Получение значения параметра узла для конкретной
	 * 					локали (языка) по имени параметра
	 * 
	 * @param parameterName имя искомого параметра
	 * @param locale из какой локали искомый параметр
	 * @return значение искомого параметра
	 * @throws LangNotSupportedException
	 */
	Object getValueLocalizedParameter(String parameterName, Locale locale) throws LangNotSupportedException;
	
	/**
	 * Получение имени параметра узла для конкретной
	 * локали (языка) по значению параметра
	 * 
	 * @param parameterValue значение искомого параметра
	 * @param locale из какой локали искомый параметр
	 * @return имя искомого параметра
	 * @throws LangNotSupportedException
	 */
	String getNameLocalizedParameter (Object parameterValue, Locale locale) throws LangNotSupportedException;
	
	/**
	 * Получение набора всех параметров узла по заданной локали
	 * 
	 * @param locale для какой локали осуществляем поиск
	 * @return набор пар (key -> value) с именами и значениями параметров
	 * @throws LangNotSupportedException
	 */
	Map<String, Object> getAllLocalizedParameters(Locale locale) throws LangNotSupportedException;
	
	/**
	 * @return список локалей, которые поддерживает узел, т.е.
	 * 		узел содержит параметры, заданные именно для этой локали
	 */
	List<Locale> getSupportedLocales();
	
	/**
	 * Проверка на поддержку узлом заданной локали
	 * 
	 * @param locale проверяемая локаль
	 * @return <code>true</code> если поддерживает, иначе <code>false</code>
	 */
	boolean isSupportedLocale(Locale locale);
	
	/**
	 * Получение первого найденного параметра с 
	 * заданным именем, не зависимо от локали
	 * 
	 * @param name имя искомого параметра
	 * @return значение искомого параметра
	 */
	Object getParameterByName(String name);
	
	/**
	 * Получение всех параметров узла всех локалей с заданным именем
	 * 
	 * @param name имя искомого параметра
	 * @return список значений искомого параметра
	 */
	List<Object> getAllParametersByName(String name);
	
	/**
	 * доступ ко всем параметрам без учета локалей
	 * @return
	 */
	Map<String, List<Object>> getAllParameters();
	
	/**
	 * Добавления нового параметра узла
	 * 
	 * @param name имя параметра
	 * @param value значение параметра
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	boolean addParameter(String name, Object value);
	
	/**
	 * Добавление локализованного параметра
	 * 
	 * @param name имя параметра
	 * @param value значение параметра
	 * @param locale для какой локали данный параметр
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	boolean addParameter(String name, Object value, Locale locale);
	
	/**
	 * Удаление всех параметров узла по имени параметра. 
	 * Исключение - представление узла.
	 * 
	 * @param name имя удаляемых параметров
	 */
	void removeParameter(String name);
	
	/**
	 * Удаление локализованного параметра
	 * 
	 * @param name имя параметра
	 * @param locale локаль в которой будет удален параметр
	 * @return объект, который ассоциировался с данным папаметром
	 * @throws LangNotSupportedException
	 */
	Object removeLocalizedParameter(String name, Locale locale) throws LangNotSupportedException;
	
	/**
	 * Удаление параметров всей локали. Некоторые параметры
	 * допустимо оставить, если они являются служебной или
	 * или иной специальной информацией об узле
	 * 
	 * @param locale удаляемая локаль
	 * @throws LangNotSupportedException
	 */
	void removeLocale (Locale locale) throws LangNotSupportedException;
	
	/**
	 * Изменение параметров узла для всех локалей
	 * 
	 * @param name имя параметра, который следует изменить. может
	 * 		быть <code>null</code> если нужен поиск по значению
	 * @param value значение параметра, который следует изменить.
	 * 		может быть <code>null</code> если нужен поиск по имени
	 * @param newValue новое значение узла. может быть <code>null</code>
	 * 		если модификации не касаются значения
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 */
	void modifyParameters (String name, Object value, Object newValue);
	
	/**
	 * Изменение параметров узла для заданной локали
	 * 
	 * @param name имя параметра, который следует изменить. может
	 * 		быть <code>null</code> если нужен поиск по значению
	 * @param value значение параметра, который следует изменить.
	 * 		может быть <code>null</code> если нужен поиск по имени.
	 * @param newValue новое значение узла. может быть <code>null</code>
	 * 		если модификации не касаются значения
	 * @param locale для какой локали будут вноситься модификации
	 * @return <code>true</code> если операция завершилась
	 * 				успешно, иначе <code>false</code>
	 * @throws LangNotSupportedException
	 */
	boolean modifyLocalizedParameter (String name, Object value, 
		Object newValue, Locale locale) throws LangNotSupportedException;
	
	/**
	 * @return полная информация об узле сети
	 */
	String toVerboseString();
	
}
