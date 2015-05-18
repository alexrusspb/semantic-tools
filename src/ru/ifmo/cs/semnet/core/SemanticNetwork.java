package ru.ifmo.cs.semnet.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Описатель семантической сети
 * 
 * @author Pismak Alexey
 * @lastUpdate 17 мая 2015 г.
 *
 * @param <T> - тип узлов с которыми будет работать сеть
 */
public interface SemanticNetwork<T extends Node> {
	
	/**
	 * @return получение ссылки на коренвой узел сети
	 */
	T getRootNode();
	
	/**
	 * Выборка понятий из сети
	 * 
	 * @param selector объект, задающий критерии выборки
	 * @return набор результатов, которые удовлетворяют условиям выборки.
	 * 		 		Набор может быть пустым, если результатов не найдено
	 */
	List<T> select(Selector selector);
	
	/**
	 * Удаление узлов сети
	 * 
	 * @param selector объект, задающий критерии поиска удаляемых узлов
	 * @param resolver объект, разрешающий конфликты ссылок при 
	 * 				модификации логической структуры данных сети
	 * @return <code>true</code> если операция успешно выполнена, 
	 * 								иначе <code>false</code>
	 */
	boolean remove(Selector selector, LinkResolver resolver);
	
	/**
	 * Вставка нового узла в сеть
	 * 
	 * @param view представление узла
	 * @param selectParent объект, который задает 
	 * 		критерии поиска родителя для нового узла
	 * @param resolver объект, разрешающий конфликты ссылок при 
	 * 				модификации логической структуры данных сети
	 * @return ссылка на добавленный узел сети
	 */
	T insert(String view, LinkResolver resolver);
	
	/**
	 * Вставка нового узла в сеть
	 * 
	 * @param view представление узла
	 * @param locale для какой локали данное представление
	 * @param selectParent объект, который задает 
	 * 		критерии поиска родителя для нового узла
	 * @param resolver объект, разрешающий конфликты ссылок при 
	 * 				модификации логической структуры данных сети
	 * @return ссылка на добавленный узел сети
	 */
	T insert(String view, Locale locale, LinkResolver resolver);
	
	/**
	 * Поиск понятий в сети, используя алгоритмы неточного 
	 * поиска и расширенные настройки выборки узлов
	 * 
	 * @param find объект, определяющий алгоритмы и критерии поиска
	 * 		узлов в семантической сети. расширяет объект выборки.
	 * @return набор результатов, которые удовлетворяют условиям неточного
	 * 		поиска при испоьзовании заданного алгоритма. Набор может быть 
	 * 		пустым, если результатов не найдено
	 */
	List<T> find(Finder find);
	
	/**
	 * Сохранение семантической сети в файл
	 * 
	 * @param file файл в который будет осуществлена запись
	 * @throws IOException
	 */
	void save(File file) throws IOException;
	
	/**
	 * Восстановление сети из файла
	 * 
	 * @param file файл из которого сеть будет выгружена
	 * @throws IOException
	 */
	void restore(File file) throws IOException;
	
	/**
	 * @return количество узлов в сети
	 */
	long sizeNetwork();
}
