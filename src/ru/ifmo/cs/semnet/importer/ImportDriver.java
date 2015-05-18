package ru.ifmo.cs.semnet.importer;

import java.io.Serializable;

import ru.ifmo.cs.semnet.core.Node;

/**
 * Описатель поведения импортера
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 * @param <T> - тип поставляемых узлов
 */
public interface ImportDriver<T extends Node> extends Serializable {
	
	/**
	 * Сканирование на наличие обновлений
	 * @return true если обновления есть, иначе false
	 */
	boolean hasUpdate();
	
	/**
	 * Возвращает очередной пакет доступного обновления,
	 * т.е. объект с различными параметрами, который может 
	 * быть будет включен в сеть, если его параметры валидны
	 * @return
	 */
	ImportPackage<T> getNextPackage();
	
	/**
	 * 
	 * @param listener
	 */
	void subscribeOnImportEvent(ImportListener listener);
}
