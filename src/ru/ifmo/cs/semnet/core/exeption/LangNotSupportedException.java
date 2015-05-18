package ru.ifmo.cs.semnet.core.exeption;

import java.util.Locale;

import ru.ifmo.cs.semnet.core.Node;

public class LangNotSupportedException extends Exception {

	private static final long serialVersionUID = -5629073219086613385L;

	private static final String MESSAGE_BODY = "Node [%s] not support language of locale [%s]";
	
	private Locale locale;
	
	private Node node;
	
	public LangNotSupportedException(Locale loc, Node source) {
		locale = loc;
		node = source;
	}

	@Override
	public String getMessage() {
		return String.format(MESSAGE_BODY, node.toString(), locale.toString());
	}
	
}
