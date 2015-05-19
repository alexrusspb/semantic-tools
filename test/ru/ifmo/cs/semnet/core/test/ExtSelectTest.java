package ru.ifmo.cs.semnet.core.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.TypeLink;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;
import ru.ifmo.cs.semnet.core.select.ExtSelectBuilder;
import ru.ifmo.cs.semnet.core.select.SelectBuilder;

public class ExtSelectTest {

protected Map<Long, DefaultNode> storage;
	
	protected DefaultNode root;
	
	protected DefaultNode child1;
	
	protected DefaultNode child2;
	
	protected long ID_ROOT = 123654L;
	
	protected long ID_CHILD_1 = 654789L;
	
	protected long ID_CHILD_2 = 369852L;
	
	@Before
	public void init() {
		storage = new HashMap<Long, DefaultNode>();
		
		root = new DefaultNode(storage, "Сущность", ID_ROOT);
		root.addParameter(SemNetUtils.VIEW_NAME_PARAMETER, "Entity", Locale.ENGLISH);
		storage.put(ID_ROOT, root);
		
		child1 = new DefaultNode(storage, "Частица", ID_CHILD_1);
		storage.put(ID_CHILD_1, child1);
		
		child2 = new DefaultNode(storage, "Крупица", ID_CHILD_2);
		storage.put(ID_CHILD_2, child2);
		
		root.addChild(ID_CHILD_1);
		root.addChild(ID_CHILD_2);
		
		child1.addLink(ID_CHILD_2, TypeLink.SYNONYM);
		child2.addLink(ID_CHILD_1, TypeLink.SYNONYM);
	}
	
	@Test
	public void testCheckNode() {
		Selector selector = ExtSelectBuilder
				.select()
				.ext(TypeLink.PARENT,  SelectBuilder
						.select()
						.param(SemNetUtils.VIEW_NAME_PARAMETER, "Entity")
						.build())
				.build();
		assertTrue(selector.checkNode(child1));
	}

}
