package ru.ifmo.cs.semnet.importer;

/**
 * Режимы сканирования обновлений, при
 * постоянной работе менеджера импорта
 * в фоновом режиме (отдельном потоке)
 * 
 * @author Pismak Alexey
 * @lastUpdate 15 мая 2015 г.
 */
public enum ImportMode {
	
	ASYNC,
	
	SYNC,
	
	/**
	 * Used as SYNC
	 */
	DEFAULT;
	
}
