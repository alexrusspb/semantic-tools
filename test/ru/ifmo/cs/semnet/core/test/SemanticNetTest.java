package ru.ifmo.cs.semnet.core.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.impl.SemNet;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;
import ru.ifmo.cs.semnet.core.resolve.InsertAsNewParentResolver;
import ru.ifmo.cs.semnet.core.resolve.InsertChildLinkResolver;
import ru.ifmo.cs.semnet.core.resolve.InsertWithMoveChildResolver;
import ru.ifmo.cs.semnet.core.select.SelectBuilder;

public class SemanticNetTest {

	private SemNet semNet;
	
	@Before
	public void init() {
		semNet = new SemNet("Сущность", SemNetUtils.RUSSIA);
		semNet.insert("Сковородка", new InsertChildLinkResolver<>(0)).addParameter("Area", "КуХня");
		semNet.insert("Кастрюля", new InsertChildLinkResolver<>(0)).addParameter("Area", "КУХНЯ");
		semNet.insert("Плитка", new InsertChildLinkResolver<>(0)).addParameter("Area", "КухнЯ");
		semNet.insert("Тарелка", new InsertChildLinkResolver<>(0)).addParameter("Area", "Ку");
	}
	
	@Ignore
	@Test
	public void testGetRootNode() {
		System.out.println(semNet.getRootNode().toVerboseString());
	}

	@Ignore
	@Test
	public void testSelect() {
		List<DefaultNode> list = semNet.select(SelectBuilder.select()
				.param("Area", "КУХНЯ")
				.build());
		System.out.println(list.toString());
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
		
		semNet.insert("Particle", Locale.ENGLISH, new InsertChildLinkResolver<DefaultNode>(0));
		
		assertTrue(semNet.sizeNetwork() == 2);
	}
	
	@Ignore
	@Test
	public void testInsertAsNewParent() {
		semNet.insert("Particle", Locale.ENGLISH, new InsertChildLinkResolver<DefaultNode>(0));
		semNet.insert("Крупица", SemNetUtils.RUSSIA, new InsertChildLinkResolver<DefaultNode>(0));
		
		Node n = semNet.insert("Кроха", SemNetUtils.RUSSIA, new InsertAsNewParentResolver<DefaultNode>(0));
		
		assertTrue(n.getChilds().size() == 2 
				&& n.getParentNode().getId() == 0 
				&& semNet.getRootNode().getChilds().size() == 1);
	}
	
	@Ignore
	@Test
	public void testInsertWithMoveChild() {
		semNet.insert("Particle", Locale.ENGLISH, new InsertChildLinkResolver<DefaultNode>(0));
		semNet.insert("Крупица", SemNetUtils.RUSSIA, new InsertChildLinkResolver<DefaultNode>(0));
		semNet.insert("Кроха", SemNetUtils.RUSSIA, new InsertAsNewParentResolver<DefaultNode>(0));
		
		Node n = semNet.insert("Пылинка", new InsertWithMoveChildResolver<>(3, 1));
		System.out.println(n.toVerboseString());
	}

	@Test
	public void testFind() {
		
	}
	
	private static final String FILE_PATH = "/home/alex/semnet.dat";
	
	@Ignore
	@Test
	public void saveText() {
		try {
			File file = new File(FILE_PATH);
			if(!file.exists()) {
				file.createNewFile();
			}
			semNet.save(file);
			System.out.println("save nodes count: " + semNet.sizeNetwork());
			
			
			semNet = new SemNet(new File(FILE_PATH));
			System.out.println("before = " + semNet.sizeNetwork());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
