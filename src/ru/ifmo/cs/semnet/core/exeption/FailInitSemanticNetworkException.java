package ru.ifmo.cs.semnet.core.exeption;

public class FailInitSemanticNetworkException extends RuntimeException {

	private static final long serialVersionUID = -1112181052640943055L;
	
	public FailInitSemanticNetworkException(Exception ex) {
		initCause(ex);
	}
}
