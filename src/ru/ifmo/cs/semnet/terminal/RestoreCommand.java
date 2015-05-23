package ru.ifmo.cs.semnet.terminal;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;

public class RestoreCommand extends CommandHandler<DefaultNode> {

	public RestoreCommand(SemanticNetwork<DefaultNode> semNet,
			PrintStream streamForResult) {
		super(semNet, streamForResult);
	}

	@Override
	public String getName() {
		return "restore";
	}

	@Override
	public void execute(String argCmd) {
		String[] args = argCmd.split(" ");
		if(args.length > 2) {
			getOutput().println("Syntax error: using: restore [ path_to_file ] ;");
			return;
		}
		String path = DEFAULT_PATH_TO_DICT_FILE;
		if(args.length > 1) {
			path = args[1];
		}
		try {
			File f = new File(path);
			getNetwork().restore(f);
			getOutput().println("Restore complete");
		} catch (IOException e) {
			getOutput().println("Can not restore from file cause: " + e.getMessage());
		}
	}

}
