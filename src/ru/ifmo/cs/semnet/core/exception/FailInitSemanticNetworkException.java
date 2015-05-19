package ru.ifmo.cs.semnet.core.exception;

/**
 * Высокоуровневое исключение. Дает понять что инициализация сети
 * завершилась неудачей. Актуально для инициализации сети из файла.
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class FailInitSemanticNetworkException extends RuntimeException {

	private static final long serialVersionUID = -1112181052640943055L;
	
	/**
	 * Инициализация объекта исключения
	 * 
	 * @param ex исключение низкого уровня (причина)
	 */
	public FailInitSemanticNetworkException(Exception ex) {
		initCause(ex);
	}
}
