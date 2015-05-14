package ru.ifmo.cs.semnet.core.test;

import java.util.Locale;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.ifmo.cs.semnet.core.Node;

public class NodeTests {

	private Node node;
	
	@Before
	public void init() {
		node = new Node("Сущность", Node.RUSSIA);
		
		node.insertParam(Node.RUSSIA, "род", "женский");
		node.insertParam(Locale.ENGLISH, Node.VIEW_OPTION, "Entity");
		node.insertParam(Node.RUSSIA, "число", "единственное");
		
		node.addChild(new Node("Частица", Node.RUSSIA));
	}
	
	@Test
	public void reomveChildTest() {
		
		Node nn = new Node("Организм", Node.RUSSIA);
		
		node.addChild(nn);
		
		System.out.println(node.toVerboseString());
		
		node.removeChild("Организм", Node.RUSSIA);
		
		System.out.println(node.toVerboseString());
	}
	
	@Ignore
	@Test
	public void testToVerboseString() {
		System.out.println(node.toVerboseString());
	}

}
