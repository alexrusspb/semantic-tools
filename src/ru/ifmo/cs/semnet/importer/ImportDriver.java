package ru.ifmo.cs.semnet.importer;

import java.io.ObjectInputStream;
import java.io.Serializable;

import ru.ifmo.cs.semnet.core.Node;

/**
 * Описатель поведения импортера
 * 
 * @author alex
 *
 * @param <T> - тип поставляемых узлов
 */
public interface ImportDriver<T extends Node> extends Serializable {
	
	/**
	 * Сканирование на наличие обновлений
	 * @return true если обновления есть, иначе false
	 */
	boolean hasUpdate();
	
	/**
	 * Возвращает очередной элемент доступного обновления,
	 * т.е. очередной доступный и уже сформированный узел.
	 * @return
	 */
	T getNextNodeItem();
	
	/**
	 * Проверка на количество обновлений
	 * 
	 * @return число доступных узлов
	 */
	long getCountNewEntries();
	
	/**
	 * Формирование потока объектов из доступных узлов
	 * 
	 * @return поток обновлений из готовых объектов
	 */
	ObjectInputStream getNodesStream();
	
}
