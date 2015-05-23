package ru.ifmo.cs.semnet.terminal;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ru.ifmo.cs.semnet.core.impl.SemNet;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;

public class SemNetTerminal {

	public static void main(String[] args) {
		
		SemNet semNet = new SemNet("Сущность", SemNetUtils.RUSSIA);
		
		TokenScanThread tokenStream = new TokenScanThread(System.in);
		
		tokenStream.appendHandler(new SizeCommand(semNet, System.out));
		tokenStream.appendHandler(new SaveCommand(semNet, System.out));
		tokenStream.appendHandler(new ImportCommand(semNet, System.out));
		tokenStream.appendHandler(new FindCommand(semNet, System.out));
		tokenStream.appendHandler(new RestoreCommand(semNet, System.out));
		tokenStream.appendHandler(new SelectCommand(semNet, System.out));
		
		Future<?> future = Executors.newSingleThreadExecutor().submit(tokenStream);
		while(!future.isDone()) { }
		
		System.out.println("exit... ok");
		
	}

}
