package ru.ifmo.cs.semnet.importer;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SemanticNetwork;

/**
 * Класс для управления подкачками новых семантических 
 * данных из внешних источников: из файлов, сети и т.д.
 * 
 * ** FIXME расширенные параметры настройки имрорта
 * 				+ выпилить костыль с PARENT_VIEW_OPTION
 * 
 * @author alex
 *
 */
public class ImportManager <T extends Node> {

	/* Зарегестрированные драйверы импорта */
	private ArrayList<ImportDriver<T>> regDrivers;
	
	/* Управляемая семантическая сеть */
	private SemanticNetwork<T> managedNetwork;
	
	/* пул */
	private ExecutorService importPool = null;
	
	private int cronTime = 10;
	
	private ImportManager(SemanticNetwork<T> semNet) {
		if(semNet == null) {
			throw new IllegalArgumentException("semantic network not defined");
		}
		regDrivers = new ArrayList<>();
		managedNetwork = semNet;
		importPool = Executors.newFixedThreadPool(2);
	}
	
	/**
	 * Регистрирует новый драйвер, который будет использоваться
	 * при очередном сканировании драйверов на поиск обносвлений.
	 * 
	 * @param driver - реализация источника семантических узлов
	 */
	public void registerDriver(ImportDriver<T> driver) {
		
		if(driver != null && !regDrivers.contains(driver)) {
			regDrivers.add(driver);
		}
	}
	
	/**
	 * Запуск импортирования новых данных
	 * 
	 * @param mode - режим импорта: sync | async
	 */
	public void forcedImport(ImportMode mode) {
		
		if(mode.equals(ImportMode.ASYNC)) {
			/* В асинхронном режиме производим запуск импорта в отдельном потоке */
			importPool.execute(this::importData);
		} else {
			importData();
		}
	}
	
	private boolean backgroundMode = false;
	
	/**
	 * Запуск работы импортера в автоматическом режиме
	 * с периодическим запуском импортирования новых данных
	 */
	public void runInBackgroundMode() {
		
		if(!backgroundMode) {
			backgroundMode = true;
		} else {
			// если запущен, то выходим
			return;
		}
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			
			@Override
			public void run() {
				
				long elapsedTime = 0;
				long startTime = new Date().getTime();
				
				while(true) {
					
					/* считаем время с последнего запуска сканирования драйверов */
					long currentTime = new Date().getTime();
					elapsedTime = currentTime - startTime;
					
					// раз в назначенное время запускаем update
					if(elapsedTime > 10 * 1000) {
						forcedImport(ImportMode.ASYNC);
						elapsedTime = 0;
						startTime = new Date().getTime();
					}
				}
			}
		});
	}
	
	/* 
	 * метод организует обход всех зарегистрированных драйверов,
	 * проверяет наличие обновлений и если обновления есть, то
	 * производит вставку новых данных в контролируемую импортером сеть
	 */
	private void importData() {
		for(ImportDriver<T> driver : regDrivers) {
			if(driver.hasUpdate()) {
				
				/* считываем очередной узел */
				ImportPackage<T> next = driver.getNextPackage();
				
				/* пока есть узлы, которые надо прочитать */
				while(next != null) {
					if(validatePackage(next)) {
						
						
						
						managedNetwork.insert(next.getView(), next.getResolver());
						next = driver.getNextPackage();
					}
				}
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		importPool.shutdownNow();
		super.finalize();
	}
	
	protected boolean validatePackage(ImportPackage<T> ip) {
		if(ip.getView() != null && ip.getResolver() != null) {
			return true;
		}
		return false;
	}
	
	public int getCronTime() {
		return cronTime;
	}

	public void setCronTime(int cronTime) {
		if(cronTime <= 0) {
			return;
		}
		this.cronTime = cronTime;
	}
}
