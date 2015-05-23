package ru.ifmo.cs.semnet.terminal;

import java.io.PrintStream;
import java.util.List;

import ru.ifmo.cs.semnet.core.Finder;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.select.FindBuilder;
import ru.ifmo.cs.semnet.core.select.Selectors;

public class FindCommand extends CommandHandler<DefaultNode> {

	public FindCommand(SemanticNetwork<DefaultNode> semNet,
			PrintStream streamForResult) {
		super(semNet, streamForResult);
	}

	@Override
	public String getName() {
		return "find";
	}

	@Override
	public void execute(String argCmd) {
		String[] args = argCmd.split(" ");
		if(args.length != 4 && args.length != 2) {
			getOutput().println("Syntax error: using: find value_param [ in param_name ];");
			return;
		}
		
		Finder finder = null;
		
		if(args.length == 2) {
			finder = Selectors.createFindByView(args[1]);
		} else {
			if(!args[2].equalsIgnoreCase("in")) {
				getOutput().print("Syntax error: using: find value_param [ in param_name ];");
				return;
			}
			finder = FindBuilder.find(args[3], args[1]).build(null);
		}
		
		List<DefaultNode> n = getNetwork().find(finder);
		
		if(n == null || n.isEmpty()) {
			getOutput().println("Not found nodes");
			return;
		}
		for(Node node : n) {
			printFindedNode(node);
		}
	}

	private void printFindedNode(Node node) {
		StringBuilder sb = new StringBuilder();
		Node scanNode = node;
		int tabs = 0;
		do {
			for(int j = 0; j < tabs; ++j) {
				sb.append("\t");
			}
			sb.append(scanNode.getView());
			sb.append(" [ID: ");
			sb.append(scanNode.getId());
			sb.append("]\n");
			tabs++;
			scanNode = scanNode.getParentNode();
		} while(scanNode != null);
		
		
		
		getOutput().println(sb.toString());
	}
}
