package ru.ifmo.cs.semnet.importer;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.ifmo.cs.semnet.core.ModifyOptions;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.Options;
import ru.ifmo.cs.semnet.core.SelectorBuilder;
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
public class ImportManager {

	/* Зарегестрированные драйверы импорта */
	private ArrayList<ImportDriver<? extends Node>> regDrivers;
	
	/* Управляемая семантическая сеть */
	private SemanticNetwork<? extends Node> managedNetwork;
	
	private ExecutorService importPool = null;
	
	private ImportManager(SemanticNetwork<? extends Node> semNet) {
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
	public void registerDriver(ImportDriver<? extends Node> driver) {
		
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
			// если запущен, то dвыходим
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
					if(elapsedTime > Options.getInstance().getCronTimeForUpdateImport() * 1000) {
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
		for(ImportDriver<?> driver : regDrivers) {
			if(driver.hasUpdate()) {
				
				/* считываем очередной узел */
				Node next = driver.getNextNodeItem();
				
				/* пока есть узлы, которые надо прочитать */
				while(next != null) {
					
					/* извлекаем представление родительского узла */
					Object parent = next.selectParamInSafemode(next.getDefaultLocale(), Node.PARENT_VIEW);
					
					Node n = null;
					
					/* ищем его в сети */
					if(parent != null) {
						n = managedNetwork.select(SelectorBuilder.Create(next.getDefaultLocale())
								.appendParameter(Node.VIEW_OPTION, parent.toString()).build());
					}
					
					ModifyOptions mo = null;
					
					/* 
					 * если родитель найден в сети, то новый узел будет вставлен
					 * между родителем и его "детьми" с полным перемещением сылок.
					 * если такого родителя нет в сети, то узел добавится к root.
					 */
					if(n != null) {
						mo = ModifyOptions.CreatEasyInsertOptions(n);
					} else {
						mo = new ModifyOptions();
						mo.setChildForNode(true);
						mo.setKeyNode(managedNetwork.getRootNode());
					}
					
					managedNetwork.insert(next, mo);
					
					next = driver.getNextNodeItem();
				}
			}
		}
	}
	
	/**
	 * Создает менеджер импорта данных для заданной сети
	 * 
	 * @param semNet - сеть, которая будет контролироваться
	 * 										данным импортером
	 * @return - экземпляр импортера
	 */
	public static ImportManager createFor(SemanticNetwork<? extends Node> semNet) {
		if(semNet == null) {
			throw new IllegalArgumentException("Не задана семаническая сеть");
		}
		return new ImportManager(semNet);
	}
	
	@Override
	protected void finalize() throws Throwable {
		importPool.shutdownNow();
		super.finalize();
	}
}
