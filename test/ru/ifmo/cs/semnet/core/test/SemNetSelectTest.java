package ru.ifmo.cs.semnet.core.test;

import java.util.Collection;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.NodeLink;
import ru.ifmo.cs.semnet.core.SelectorBuilder;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SemNetSelectTest {

	private SemanticNetwork<Node> semNet;
	
	@Before
	public void init() {
		Node root = new Node("сущность", Node.RUSSIA);
		root.insertParam(Node.RUSSIA, "compute", "compute");
		semNet = new SemanticNetwork<Node>(root);
		
		Node prog = new Node("программа", Node.RUSSIA);
		prog.insertParam(Node.RUSSIA, "compute", "compute");
		root.getLinks().put(prog, NodeLink.CHILD);
	}
	
	@Test
	public void selectTest() {
		Node node = semNet.select (
				SelectorBuilder
				.Create(Node.RUSSIA)
				.appendParameter(Node.VIEW_OPTION, "программа")
				.build()
				);
		assertTrue(node != null);
	}
	
	@Test
	public void multySelectTest() {
		Collection<Node> node = semNet.selectAll (
				SelectorBuilder
				.Create(Node.RUSSIA)
				.appendParameter("compute", "compute")
				.build()
				);
		assertTrue(node.size() == 2);
	}
	
	@After
	public void garbageCollect() {
		semNet = null;
	}

}
