package ru.ifmo.cs.semnet.core.test;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import ru.ifmo.cs.semnet.core.ModifyOptions;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SemanticNetwork;

public class SemNetInsertTest {
	
	private SemanticNetwork<Node> semNet;
	
	private Node rtNode;
	
	@Before
	public void init() {
		rtNode = new Node("Entity", Locale.ENGLISH);
		semNet = new SemanticNetwork<Node>(rtNode);
	}
	
	@Test
	public void testInsert() {
		
		ModifyOptions mo = new ModifyOptions();
		mo.setChildForNode(true);
		mo.setKeyNode(rtNode);
		
		semNet.insert(new Node("Particle", Locale.ENGLISH), mo);
		semNet.insert(new Node("Personal", Locale.ENGLISH), mo);
		semNet.insert(new Node("Fish", Locale.ENGLISH), mo);
		semNet.insert(new Node("Doll", Locale.ENGLISH), mo);
		
		long size = semNet.size();
		
		System.out.println(rtNode.toVerboseString());
		
		assertTrue(size == 5);
	}

}
