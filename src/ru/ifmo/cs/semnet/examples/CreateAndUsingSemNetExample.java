package ru.ifmo.cs.semnet.examples;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.importer.ImportManager;
import ru.ifmo.cs.semnet.importer.impl.InteractiveImportDriver;

public class CreateAndUsingSemNetExample {

	public static void main(String[] args) {
		
		
		Node root = new Node("Сущность", Node.RUSSIA);
		
		SemanticNetwork<Node> semanticNetwork = new SemanticNetwork<Node>(root);
		
		InteractiveImportDriver driver = new InteractiveImportDriver(System.in, Node.RUSSIA);
		
		ImportManager manager = ImportManager.createFor(semanticNetwork);
		manager.registerDriver(driver);
		manager.runInBackgroundMode();

		System.out.println("Для импорта данных используйте формат ввода:");
		System.out.println(driver.toString());
		System.out.println("Для выхода введите EXIT");
		
		while(driver.isWorked()) {
			// ждем, пока работает драйвер
		}
		
		System.out.println("Размер сети: " + semanticNetwork.size());
		System.out.println("Корневой узел имеет формат: \n" + root.toVerboseString());
	}

}
