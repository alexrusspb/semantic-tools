package ru.ifmo.cs.semnet.importer.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.Options;
import ru.ifmo.cs.semnet.importer.ImportDriver;

public class InteractiveImportDriver implements ImportDriver<Node> {

	private static final long serialVersionUID = -2059568814402703495L;

	private static final String IMPORT_KEYWORD = "IMPORT";
	
	private static final String FOR_KEYWORD = "FOR";
	
	private static final String LOCALE_KEY = "-L";
	
	private ConcurrentLinkedQueue<Node> updates = new ConcurrentLinkedQueue<>();
	
	private Scanner scanner = null;
	
	private Locale importForLocale = null;
	
	public InteractiveImportDriver(InputStream source, Locale loc) {
		if(source != null) {
			scanner = new Scanner(source);
			if(loc == null) {
				importForLocale = Options.getInstance().getDefaultLocale();
			} else {
				importForLocale = loc;
			}
			Executors.newSingleThreadExecutor().execute(this::updateCycle);
		}
	}

	private boolean worked = false;
	
	public boolean isWorked() {
		return worked;
	}
	
	protected void updateCycle() {
		worked = true;
		while(true) {
			if(scanner != null && scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] args = line.split(" ");
				
				if(args.length == 1 && args[0].equalsIgnoreCase("EXIT")) {
					scanner.close();
					worked = false;
					return;
				}
				
				if(args.length >= 2) {
					for(String s : args) {
						s.toUpperCase();
					}
					processImport(args);
				}
			}
		}
	}
	
	protected void processImport(String[] args) {
		if(args[0].equals(IMPORT_KEYWORD)) {
			
			String view = args[1];
			Locale l = importForLocale;
			HashMap<String, String> params = new HashMap<>();
			
			ArrayList<String> argsList = (ArrayList<String>) Arrays.asList(args);
			argsList.remove(0);
			argsList.remove(1);
			
			if(argsList.contains(LOCALE_KEY)) {
				int lastIndexOf = argsList.lastIndexOf(LOCALE_KEY);
				l = new Locale(argsList.get(lastIndexOf + 1));
				argsList.remove(lastIndexOf + 1);
				argsList.remove(lastIndexOf);
			}
			
			if(argsList.contains(FOR_KEYWORD)) {
				int lastIndexOf = argsList.lastIndexOf(FOR_KEYWORD);
				params.put(Node.PARENT_VIEW, argsList.get(lastIndexOf + 1));
				argsList.remove(lastIndexOf + 1);
				argsList.remove(lastIndexOf);
			}
			
			if(argsList.size() > 0) {
				for(String param : argsList) {
					String[] opts = param.split("=");
					if(opts.length != 2) {
						continue;
					}
					params.put(opts[0], opts[1]);
				}
			}
			
			Node newNode = new Node(view, l);
			for(String parameter : params.keySet()) {
				newNode.insertParam(l, parameter, params.get(parameter));
			}
			updates.add(newNode);
		}
	}
	
	@Override
	public boolean hasUpdate() {
		return updates.size() > 0;
	}

	@Override
	public Node getNextNodeItem() {
		return updates.poll();
	}

	@Override
	public long getCountNewEntries() {
		return updates.size();
	}

	@Override
	public ObjectInputStream getNodesStream() {
		ObjectInputStream ois = null;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
		    ObjectOutputStream oos = new ObjectOutputStream(baos);
	
		    while(updates.size() > 0) {
			    oos.writeObject(updates.poll());
		    }
	
		    oos.flush();
		    oos.close();

		    InputStream is = new ByteArrayInputStream(baos.toByteArray());
		    ois = new ObjectInputStream(is);
		} catch (IOException ex) {
			return null;
		}
	    
		return ois;
	}

	@Override
	public String toString() {
		return "usage: IMPORT VIEW_OF_NEW_NODE [FOR VIEW_OF_PARENT_NODE] \n"
				+ "		[OPT_NAME=OPT_VALUE ...] [-L locale_code]\n";
	}
	
}
