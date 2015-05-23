package ru.ifmo.cs.semnet.terminal;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TokenScanThread implements Runnable {
	
	private InputStream inputStream;
	
	private Map<String, CommandHandler<?>> commands;
	
	private static final String CLOSE_KEYWORD = "Exit"; 
	
	private static final String COMMANDS_DELIMETER = ";";
	
	public TokenScanThread(InputStream source) {
		inputStream = source;
		commands = new HashMap<>();
	}
	
	@SuppressWarnings("resource")
	@Override
	public void run() {
		String command = null;
		Scanner scanner = new Scanner(inputStream);
		do {
			if(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if(!line.contains(COMMANDS_DELIMETER)) {
					command = new StringBuilder(command == null ? "" : command)
								.append(" ")
								.append(line)
								.toString().trim();
					continue;
				}
				
				if(command != null) {
					line = (command + " " + line).trim();
				}
				
				while(line.contains(COMMANDS_DELIMETER)) {
					String[] cmds = line.split(COMMANDS_DELIMETER);
					if(cmds.length > 1) {
						StringBuilder builder = new StringBuilder();
						for(int i = 1; i < cmds.length; ++i) {
						    builder.append(cmds[i]);
						    builder.append(COMMANDS_DELIMETER);
						}
						line = builder.toString().trim();
					} else {
						line = "";
					}
					
					if(cmds == null || cmds.length == 0) {
						continue;
					}
					
					try {
						command = runCommand(cmds[0]);
					} catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}
					if(!command.equalsIgnoreCase(CLOSE_KEYWORD)) {
						command = null;
					}
				}
			}

		} while (command == null || !command.equalsIgnoreCase(CLOSE_KEYWORD));
	}
	
	public void appendHandler(CommandHandler<?> cmd) {
		if(cmd != null && !commands.containsKey(cmd.getName())) {
			commands.put(cmd.getName(), cmd);
		}
	}

	private String runCommand(String string) {
		String[] parts = string.split(" ");
		
		for(String s : commands.keySet()) {
			if(parts[0].equalsIgnoreCase(s)) {
				commands.get(s).execute(string);
				break;
			}
		}
		return parts[0];
	}
}
