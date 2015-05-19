package ru.ifmo.cs.semnet.importer.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ru.ifmo.cs.semnet.importer.ImportDriver;
import ru.ifmo.cs.semnet.importer.ImportListener;
import ru.ifmo.cs.semnet.importer.ImportPackage;

/**
 * Реализация простейшего потокового драйвера импорта.
 * Драйвер осуществляет чтение входной строки из заданного
 * потока ввода, согласно формату, указанному в helpString();
 * 
 * Завершается работа драйвера подачей строки EXIT на входной
 * поток текстовых данных.
 * 
 * FIXME добавить нормальный ридер и конвертацию processImport
 * 
 * @author alex
 *
 */
public class TextStreamImportDriver<T extends ImportPackage> implements ImportDriver<T> {

	private static final long serialVersionUID = -2059568814402703495L;

	private static final String IMPORT_KEYWORD = "IMPORT";
	
	private static final String FOR_KEYWORD = "FOR";
	
	private static final String LOCALE_KEY = "-L";
	
	/* считанные ноды */
	private ConcurrentLinkedQueue<T> updates = new ConcurrentLinkedQueue<>();
	
	private Scanner scanner = null;
	
	private Locale importForLocale = null;
	
	/**
	 * Инициализация драйвера
	 * 
	 * @param source - поток-источник текстовых строк (не может быть null)
	 * @param loc	 - локаль по-умолчанию для которой добавляются узлы сети
	 */
	public TextStreamImportDriver(InputStream source, Locale loc) {
		if(source != null) {
			scanner = new Scanner(source);
			if(loc == null) {
				importForLocale = Locale.getDefault();
			} else {
				importForLocale = loc;
			}
			start();
		}
	}
	
	/**
	 * Запуск драйвера. Вызывается автоматически, после создания
	 * экземпляра драйвера. Вызов "вручную" необходим, если работа
	 * драйвера была прекращена.
	 */
	public void start() {
		if(worked == null || !isWorked()) {
			worked = Executors.newSingleThreadExecutor().submit(this::updateCycle);
		}
	}

	private Future<?> worked = null;
	
	/**
	 * Определение состояния драйвера.
	 * @return true, если драйвер активен и воспринимает 
	 * 				сканирует входящий поток, иначе false
	 */
	public boolean isWorked() {
		return !worked.isDone();
	}
	
	/**
	 * Метод осуществляющий сканирование потока в бесконечном цикле,
	 * а так же прекращает его работу по необходимости.
	 */
	protected void updateCycle() {
		while(true) {
			if(scanner != null && scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] args = line.split(" ");
				
				if(args.length == 1 && args[0].equalsIgnoreCase("EXIT")) {
					System.out.println("closing and exit");
					return;
				}
				
				if(args.length >= 2) {
					processImport(args);
				}
			}
		}
	}
	
	/**
	 * Метод разбора входящей строки и создание нового
	 * узла сети, на основе заданных параметров
	 * @param args
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	protected void processImport(String[] args) {
		if(args[0].equals(IMPORT_KEYWORD)) {
			
			String view = args[1];
			Locale l = importForLocale;
			Map<String, String> params = new HashMap<>();
			String parent = null;
			
			/* конвертируем в лист, ибо так будет проще */
			List<String> argsList = new ArrayList<>();
			/* слово IMPORT и представление ноды опускаем */
			for(int i = 2; i < args.length; ++i) {
				argsList.add(args[i]);
			}
			
			/* меняем локаль, если она заданна */
			if(argsList.contains(LOCALE_KEY)) {
				int lastIndexOf = argsList.lastIndexOf(LOCALE_KEY);
				l = new Locale(argsList.get(lastIndexOf + 1));
				argsList.remove(lastIndexOf + 1);
				argsList.remove(lastIndexOf);
			}
			
			/* задаем родительский узел */
			if(argsList.contains(FOR_KEYWORD)) {
				int lastIndexOf = argsList.lastIndexOf(FOR_KEYWORD);
				parent = argsList.get(lastIndexOf + 1);
				argsList.remove(lastIndexOf + 1);
				argsList.remove(lastIndexOf);
			}
			
			/* если заданы доп. параметры, то сохраняем и их */
			if(argsList.size() > 0) {
				for(String param : argsList) {
					String[] opts = param.split("=");
					if(opts.length != 2) {
						continue;
					}
					params.put(opts[0], opts[1]);
				}
			}
			
			ImportPackage pack = new ImportPackageImpl();
			pack.setView(view);
			pack.setParentView(parent);
			
			updates.add((T) pack);
		}
	}
	
	@Override
	public boolean hasUpdate() {
		return updates.size() > 0;
	}

	@Override
	public ObjectInputStream getPackagesStream() {
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

	public String helpString() {
		return "usage: IMPORT VIEW_OF_NEW_NODE [FOR VIEW_OF_PARENT_NODE] \n"
				+ "		[OPT_NAME=OPT_VALUE ...] [-L locale_code]\n";
	}
	
	@Override
	public String toString() {
		return helpString();
	}

	@Override
	public T getNextPackage() {
		return updates.poll();
	}

	@Override
	public void subscribeOnImportEvent(ImportListener listener) {
		// TODO
	}
	
}
