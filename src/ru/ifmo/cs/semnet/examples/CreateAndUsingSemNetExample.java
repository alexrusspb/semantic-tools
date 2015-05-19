package ru.ifmo.cs.semnet.examples;

import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.impl.SemNet;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;
import ru.ifmo.cs.semnet.importer.ImportManager;
import ru.ifmo.cs.semnet.importer.impl.TextStreamImportDriver;

public class CreateAndUsingSemNetExample {

	public static void main(String[] args) {
		
		SemNet semanticNetwork = new SemNet("Сущность", SemNetUtils.RUSSIA);
		
		TextStreamImportDriver<DefaultNode> driver = new TextStreamImportDriver<>(System.in, SemNetUtils.RUSSIA);
		
		ImportManager<DefaultNode> manager = new ImportManager<DefaultNode>(semanticNetwork);
		manager.registerDriver(driver);
		manager.runInBackgroundMode();

		System.out.println("Для импорта данных используйте формат ввода:");
		System.out.println(driver.helpString());
		System.out.println("Для выхода введите EXIT");
		
		while(driver.isWorked()) {
			// ждем, пока работает драйвер
			// пока юзер не нажмет EXIT
		}
		
		System.out.println("Размер сети: " + semanticNetwork.sizeNetwork());
		System.out.println("Корневой узел имеет формат: \n" + semanticNetwork.getRootNode().toVerboseString());
	}
}
