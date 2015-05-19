package ru.ifmo.cs.semnet.importer;

import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * Описатель поведения импортера
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 * @param <T> - тип поставляемых узлов
 */
public interface ImportDriver<T extends ImportPackage> extends Serializable {
	
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
	T getNextPackage();
	
	/**
	 * Подписка на обработку обновлений по событию
	 * 
	 * @param listener слушатель события обновления
	 */
	void subscribeOnImportEvent(ImportListener listener);
	
	/**
	 * @return возвращает все доступные обновления в
	 * 		виде потока пакетов обновления
	 */
	ObjectInputStream getPackagesStream();
}
