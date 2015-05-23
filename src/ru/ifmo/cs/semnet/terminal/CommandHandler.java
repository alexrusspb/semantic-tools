package ru.ifmo.cs.semnet.terminal;

import java.io.PrintStream;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SemanticNetwork;

public abstract class CommandHandler<T extends Node> {
	
	public static final String DEFAULT_PATH_TO_DICT_FILE = "semnet.db";
	
	private SemanticNetwork<T> handledSemNet;
	
	private PrintStream outputStream;
	
	public abstract String getName();
	
	public CommandHandler(SemanticNetwork<T> semNet, PrintStream streamForResult) {
		handledSemNet = semNet;
		outputStream = streamForResult;
	}
	
	protected SemanticNetwork<T> getNetwork() {
		return handledSemNet;
	}
	
	protected PrintStream getOutput() {
		return outputStream;
	}
	
	public abstract void execute(String argCmd);
}
