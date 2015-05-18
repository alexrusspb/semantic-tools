package ru.ifmo.cs.semnet.core.exception;

import java.util.Locale;

import ru.ifmo.cs.semnet.core.Node;

/**
 * Исключение, информиующее, что при выполнении операции 
 * с локализованными данными произошла ошибка доступа к
 * данным, т.к. запрашиваемая локаль не поддерживается.
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class LangNotSupportedException extends Exception {

	private static final long serialVersionUID = -5629073219086613385L;

	private static final String MESSAGE_BODY = "Node [%s] not support language of locale [%s]";
	
	/* запрашиваемая локаль */
	private Locale locale;
	
	/* в каком узле произошло исключение */
	private Node node;
	
	/**
	 * Инициализация объекта исключения
	 * 
	 * @param loc запрошенная локаль
	 * @param source узел сети - источник исключения
	 */
	public LangNotSupportedException(Locale loc, Node source) {
		locale = loc;
		node = source;
	}

	@Override
	public String getMessage() {
		return String.format(MESSAGE_BODY, node.toString(), locale.toString());
	}
}
