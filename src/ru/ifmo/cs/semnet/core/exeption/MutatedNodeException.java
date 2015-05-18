package ru.ifmo.cs.semnet.core.exeption;

import ru.ifmo.cs.semnet.core.Node;

public class MutatedNodeException extends RuntimeException {

	private static final long serialVersionUID = -972955934333948058L;
	
	private final Node mutableNode;
	
	private final String couseMutableMessage;
	
	public MutatedNodeException(Node node, String couseMutable) {
		mutableNode = node;
		couseMutableMessage = couseMutable;
	}
	
	@Override
	public String getMessage() {
		return new StringBuilder().append("Node [").append(mutableNode.getId())
				.append("] was mutated cause: ").append(couseMutableMessage).toString();
	}
}
