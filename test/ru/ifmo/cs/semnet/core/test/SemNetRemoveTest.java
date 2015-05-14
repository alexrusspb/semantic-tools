package ru.ifmo.cs.semnet.core.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.ifmo.cs.semnet.core.ModifyOptions;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SelectorBuilder;
import ru.ifmo.cs.semnet.core.SemanticNetwork;

public class SemNetRemoveTest {

	private SemanticNetwork<Node> semNet;
	
	private Node root;
	
	@Before
	public void init() {
		root = new Node("Cущность", Node.RUSSIA);
		semNet = new SemanticNetwork<Node>(root);
		
		Node particle = new Node("Частица", Node.RUSSIA);
		particle.addChild(new Node("Фотон", Node.RUSSIA));
		
		root.addChild(particle);
	}
	
	@Test
	public void testRemove() {
		System.out.println("ДО:\n" + root.toVerboseString());
		assertTrue(semNet.remove(SelectorBuilder
				.Create(Node.RUSSIA)
				.appendParameter(Node.VIEW_OPTION, "Частица")
				.build(), 
				ModifyOptions.CreateEasyRemoveOptions()) == true);
		System.out.println("\n\n\nПОСЛЕ:\n" + root.toVerboseString());
	}

	@After
	public void garbCollect() {
		semNet = null;
	}
	
}
