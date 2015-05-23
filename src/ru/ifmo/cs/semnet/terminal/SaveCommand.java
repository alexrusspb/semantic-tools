package ru.ifmo.cs.semnet.terminal;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;

public class SaveCommand extends CommandHandler<DefaultNode> {

	public SaveCommand(SemanticNetwork<DefaultNode> semNet, 
			PrintStream streamForResult) {
		super(semNet, streamForResult);
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public void execute(String argCmd) {
		String[] args = argCmd.split(" ");
		if(args.length > 2) {
			getOutput().println("Syntax error: using: save [ path_to_file ] ;");
			return;
		}
		String path = DEFAULT_PATH_TO_DICT_FILE;
		if(args.length > 1) {
			path = args[1];
		}
		try {
			File f = new File(path);
			getNetwork().save(f);
			getOutput().println("Saved to: " + f.getAbsolutePath());
		} catch (IOException e) {
			getOutput().println("Can not save file cause: " + e.getMessage());
		}
	}

}
