package ru.ifmo.cs.semnet.examples;

import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.impl.SemNet;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;
import ru.ifmo.cs.semnet.importer.ImportDriver;
import ru.ifmo.cs.semnet.importer.impl.ImportManagerImpl;
import ru.ifmo.cs.semnet.importer.impl.ImportPackageImpl;
import ru.ifmo.cs.semnet.importer.impl.TextStreamImportDriver;

public class CreateAndUsingSemNetExample {

	public static void main(String[] args) {
		
		// написать обертку для все этих телодвижений
		
		SemNet semanticNetwork = new SemNet("Сущность", SemNetUtils.RUSSIA);
		
		ImportDriver<ImportPackageImpl> driver = new TextStreamImportDriver<>(System.in, SemNetUtils.RUSSIA);
		
		ru.ifmo.cs.semnet.importer.ImportManager<ImportPackageImpl, DefaultNode> manager = new ImportManagerImpl<>(semanticNetwork);
		manager.registerDriver(driver);
		manager.runInBackgroundMode();

		System.out.println("Для импорта данных используйте формат ввода:");
		System.out.println(driver.toString());
		System.out.println("Для выхода введите EXIT");
		
		while(((TextStreamImportDriver<ImportPackageImpl>)driver).isWorked()) {
			// ждем, пока работает драйвер
			// пока юзер не нажмет EXIT
		}
		
		System.out.println("Размер сети: " + semanticNetwork.sizeNetwork());
		System.out.println("Корневой узел имеет формат: \n" + semanticNetwork.getRootNode().toVerboseString());
	}
}
