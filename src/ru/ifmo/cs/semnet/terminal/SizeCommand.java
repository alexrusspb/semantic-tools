package ru.ifmo.cs.semnet.terminal;

import java.io.PrintStream;

import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;

public class SizeCommand extends CommandHandler<DefaultNode> {

	public SizeCommand(SemanticNetwork<DefaultNode> semNet, PrintStream streamForResult) {
		super(semNet, streamForResult);
	}

	@Override
	public String getName() {
		return "size";
	}

	@Override
	public void execute(String argCmd) {
		if(argCmd.split(" ").length == 1) {
			getOutput().println("Size network: " + getNetwork().sizeNetwork() + " nodes");
		} else {
			getOutput().println("Syntax error: using: size;");
		}
	}

}
