package ru.ifmo.cs.semnet.terminal;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;
import ru.ifmo.cs.semnet.core.resolve.Resolvers;

public class ImportCommand extends CommandHandler<DefaultNode> {
	
	private static final String FOR_KEYWORD = "FOR";
	
	private static final String LOCALE_KEY = "-L";
	
	public ImportCommand(SemanticNetwork<DefaultNode> semNet,
			PrintStream streamForResult) {
		super(semNet, streamForResult);
	}

	@Override
	public String getName() {
		return "import";
	}

	@Override
	public void execute(String argCmd) {
		String[] args = argCmd.split(" ");
		
		String view = args[1];
		String parentView = null;
		Locale locale = null;
		
		args = Arrays.copyOfRange(args, 2, args.length);
		
		if(!SemNetUtils.containsWithIgnoreCase(new HashSet<>(Arrays.asList(args)), FOR_KEYWORD)) {
			getOutput().println("Syntax error: parent not set.\n\t "
					+ "using: import view_noew for view_parent_node [ -L locale_code ];");
			return;
		}
		int li = 0;
		// last index with ignore case
		for(int i = 0; i < args.length; ++i) {
			if(args[i].equalsIgnoreCase(FOR_KEYWORD)) {
				li = i;
				break;
			}
		}
		if(li + 1 == args.length) {
			getOutput().println("Syntax error: parent not set.\n\t "
					+ "using: import view_noew for view_parent_node [ -L locale_code ];");
			return;
		}
		parentView = args[li + 1];

		args = remove(args, li + 1);
		args = remove(args, li);
		
		if(SemNetUtils.containsWithIgnoreCase(new HashSet<>(Arrays.asList(args)), LOCALE_KEY)) {
			// last index with ignore case
			for(int i = 0; i < args.length; ++i) {
				if(args[i].equalsIgnoreCase(LOCALE_KEY)) {
					li = i;
					break;
				}
			}
			if(li + 1 == args.length) {
				getOutput().println("Syntax error: parent not set.\n\t "
						+ "using: import view_noew for view_parent_node [ -L locale_code ];");
				return;
			}
			locale = new Locale(args[li + 1]);
			args = remove(args, li + 1);
			args = remove(args, li);
		}
		
		Node n = getNetwork().insert(view, locale, Resolvers
				.createChildInsertResolver(getNetwork(), parentView));
		
		if(n != null) {
			getOutput().println("Insert new node with ID = " + n.getId());
		} else {
			getOutput().println("Can not insert new node");
		}
		
		if(args.length > 0) {
			for(String param : args) {
				String[] pair = param.split("=");
				if(pair.length != 2) {
					continue;
				}
				n.addParameter(pair[0], pair[1]);
			}
		}
	}
	
	private String[] remove(String[] orig, int index) {
		String[] newString = new String[orig.length - 1];
		int j = 0;
		for(int i = 0; i < orig.length; ++i) {
			if(i != index) {
				newString[j++] = orig[i];
			}
		}
		return newString;
	}

}
