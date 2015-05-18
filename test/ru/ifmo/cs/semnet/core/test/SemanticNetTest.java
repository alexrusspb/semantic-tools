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
import ru.ifmo.cs.semnet.core.TypeLink;
import ru.ifmo.cs.semnet.core.impl.DefaultNode;
import ru.ifmo.cs.semnet.core.impl.SemNet;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;
import ru.ifmo.cs.semnet.core.resolve.EasyRemoveLinkResolver;
import ru.ifmo.cs.semnet.core.resolve.InsertAsNewParentResolver;
import ru.ifmo.cs.semnet.core.resolve.InsertChildLinkResolver;
import ru.ifmo.cs.semnet.core.resolve.InsertWithMoveChildResolver;
import ru.ifmo.cs.semnet.core.select.FindBuilder;
import ru.ifmo.cs.semnet.core.select.SelectBuilder;

public class SemanticNetTest {

	private SemNet semNet;
	private DefaultNode node;
	
	@Before
	public void init() {
		semNet = new SemNet("Сущность", SemNetUtils.RUSSIA);
		node = semNet.insert("Сковородка", new InsertChildLinkResolver<>(0));
		semNet.insert("Кастрюля", new InsertChildLinkResolver<>(1));
		semNet.insert("Плитка", new InsertChildLinkResolver<>(1));
		semNet.insert("Тарелка", new InsertChildLinkResolver<>(1)).addLink(1, TypeLink.SYNONYM);
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

	
	@Test
	public void testRemove() {
		semNet.remove(SelectBuilder.select().id(node.getId()).build(), new EasyRemoveLinkResolver<>());
		System.out.println(semNet.select(SelectBuilder.select().id(0, 1, 2, 3, 4).build()));
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

	@Ignore
	@Test
	public void testFind() {
		System.out.println(
				semNet.find(
						FindBuilder.find("area", "Кухна").build(null)));
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
