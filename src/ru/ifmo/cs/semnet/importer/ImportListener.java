package ru.ifmo.cs.semnet.importer;

/**
 * Слушатель поступления обновлений.
 * Используется, если  работа менеджера
 * импорта не  предусматривает  фоновое
 * беспрерывное сканирование обновлений. 
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
@FunctionalInterface
public interface ImportListener {
	
	/**
	 * Метод передающий "свежесчитанный" пакет
	 * 
	 * @param pack данные для инициализации нового узла
	 */
	void doImport(ImportPackage pack);
	
}
