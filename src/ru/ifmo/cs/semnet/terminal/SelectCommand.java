package ru.ifmo.cs.semnet.terminal;

import java.io.PrintStream;
import java.util.List;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.select.SelectBuilder;

public class SelectCommand extends CommandHandler<DefaultNode> {

	public SelectCommand(SemanticNetwork<DefaultNode> semNet,
			PrintStream streamForResult) {
		super(semNet, streamForResult);
	}

	@Override
	public String getName() {
		return "select";
	}

	@Override
	public void execute(String argCmd) {
		String[] args = argCmd.split(" ");
		
		if(args.length < 3) {
			getOutput().println("Syntax error: using: select value by [id] | [param name=value]");
			return;
		}
		
		if(!args[2].equalsIgnoreCase("by")) {
			getOutput().println("Syntax error: using: select value by [id] | [param name=value]");
		}
		
		SelectBuilder builder = SelectBuilder.select();
		
		String value = args[1];
		
		if(args.length == 4 && args[3].equalsIgnoreCase("id")) {
			int val;
			try {
				val = Integer.parseInt(value);
				builder.id(val);
			} catch (Exception ex) {
				getOutput().println("Id is not valid");
				return;
			}
		} else if(args.length == 5 && args[3].equalsIgnoreCase("param")) {
			
			builder.param(args[4], value);
			
		} else {
			getOutput().println("Syntax error: using: select value by [id] | [param name=value]");
			return;
		}
		
		List<DefaultNode> n = getNetwork().select(builder.build());
		
		if(n == null || n.isEmpty()) {
			getOutput().println("Not found nodes");
			return;
		}
		for(Node node : n) {
			getOutput().println(node.toVerboseString());
			getOutput().println("------------------------------------------");
		}
	}
}
