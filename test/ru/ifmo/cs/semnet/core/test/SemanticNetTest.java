package ru.ifmo.cs.semnet.core.test;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.impl.SemNet;
import ru.ifmo.cs.semnet.core.impl.utils.InsertAsNewParentResolver;
import ru.ifmo.cs.semnet.core.impl.utils.InsertChildLinkResolver;
import ru.ifmo.cs.semnet.core.impl.utils.SemNetUtils;

public class SemanticNetTest {

	private SemNet semNet;
	
	@Before
	public void init() {
		semNet = new SemNet("Сущность", SemNetUtils.RUSSIA);
	}
	
	@Ignore
	@Test
	public void testGetRootNode() {
		System.out.println(semNet.getRootNode().toVerboseString());
	}

	@Ignore
	@Test
	public void testSelect() {
		
	}

	@Ignore
	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testInsertStringLocaleSelectorLinkResolver() {
		fail("Not yet implemented");
	}
	
	@Ignore
	@Test
	public void testInsertChild() {
		System.out.println(semNet.insert("Particle", Locale.ENGLISH, new InsertChildLinkResolver(0)).toVerboseString());
	}
	
	@Test
	public void testInsertAsNewParent() {
		semNet.insert("Particle", Locale.ENGLISH, new InsertChildLinkResolver(0));
		semNet.insert("Крупица", SemNetUtils.RUSSIA, new InsertChildLinkResolver(0));
		
		Node n = semNet.insert("Кроха", SemNetUtils.RUSSIA, new InsertAsNewParentResolver(0));
		
		assertTrue(n.getChilds().size() == 2 
				&& n.getParentNode().getId() == 0 
				&& semNet.getRootNode().getChilds().size() == 1);
	}

	@Ignore
	@Test
	public void testFind() {
		fail("Not yet implemented");
	}

}
