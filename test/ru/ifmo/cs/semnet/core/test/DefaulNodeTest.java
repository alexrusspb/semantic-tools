package ru.ifmo.cs.semnet.core.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.ifmo.cs.semnet.core.TypeLink;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.impl.utils.SemNetUtils;

public class DefaulNodeTest {
	
	private Map<Long, DefaultNode> storage;
	
	private DefaultNode node;
	
	private DefaultNode child;
	
	private long ID_NUMBER = 123654L;
	
	@Before
	public void init() {
		storage = new HashMap<>();
		node = new DefaultNode(storage, "Сущность", ID_NUMBER);
		child = new DefaultNode(storage, "Частица", ID_NUMBER + 1);
		storage.put(ID_NUMBER, node);
		storage.put(ID_NUMBER + 1, child);
		node.addChild(ID_NUMBER + 1);
	}
	
	@Test
	public void testGetId() {
		assertTrue(node.getId() == ID_NUMBER);
	}

	@Test
	public void testGetView() {
		assertEquals("Сущность", node.getView());
	}

	@Ignore
	@Test
	public void testGetViewLocale() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetChilds() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLinksTypeLink() {
		assertTrue(node.getLinks(TypeLink.CHILD).size() == 1);
	}

	@Ignore
	@Test
	public void testGetLinks() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testAddLink() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testRemoveLink() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testRemoveLinks() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testClearLinks() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testOnRemoveNode() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddChild() {
		assertEquals(ID_NUMBER, child.getParentNode().getId());
	}

	@Ignore
	@Test
	public void testRemoveChild() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetParentNode() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testChangeParent() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetValueLocalizedParameter() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetNameLocalizedParameter() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetAllLocalizedParameters() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllParameters() {
		node.addParameter(SemNetUtils.VIEW_NAME_PARAMETER, "Entity", Locale.ENGLISH);
		
		List<Object> list = node.getAllParameters().get(SemNetUtils.VIEW_NAME_PARAMETER);
		
		assertTrue(list.contains("Entity") && list.contains("Сущность"));
	}

	@Ignore
	@Test
	public void testGetSupportedLocales() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIsSupportedLocale() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetParameterByName() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetAllParametersByName() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testAddParameterStringObject() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testAddParameterStringObjectLocale() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testRemoveParameter() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testRemoveLocalizedParameter() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testRemoveLocale() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testModifyParameters() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testModifyLocalizedParameter() {
		fail("Not yet implemented");
	}

	@Test
	public void testToVerboseString() {
		System.out.println(node.toVerboseString());
	}

}
