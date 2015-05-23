package ru.ifmo.cs.semnet.core.exception;

/**
 * Высокоуровневое исключение. Дает понять что сеть имеет проблемы.
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class SemanticNetworkException extends RuntimeException {

	private static final long serialVersionUID = -1112181052640943055L;
	
	/**
	 * Инициализация объекта исключения
	 * 
	 * @param ex исключение низкого уровня (причина)
	 */
	public SemanticNetworkException(Exception ex) {
		initCause(ex);
	}
}
