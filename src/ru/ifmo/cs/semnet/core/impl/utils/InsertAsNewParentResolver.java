package ru.ifmo.cs.semnet.core.impl.utils;

import java.util.List;
import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.TypeLink;

public class InsertAsNewParentResolver implements LinkResolver {

	private static final long NOT_INIT = -1L;
	
	private long parentNodeId = NOT_INIT;
	
	private Selector selector;
	
	public InsertAsNewParentResolver(long parentId) {
		this.parentNodeId = parentId;
	}
	
	public InsertAsNewParentResolver(Selector select) {
		if(select == null) {
			throw new IllegalArgumentException("Selector in resolver can not be null");
		}
		selector = select;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Node> T resolve(Node modifySource, Map<Long, T> storage,
			SemanticNetwork<T> semNet) {
		Node parent = null;
		if(parentNodeId != NOT_INIT) {
			parent = storage.get(parentNodeId);
		} else {
			parent = semNet.select(selector).get(0);
		}
		List<Node> list = parent.getChilds();
		parent.removeLinks(TypeLink.CHILD);
		parent.addChild(modifySource.getId());
		for(Node n : list) {
			modifySource.addChild(n.getId());
		}
		return (T)modifySource;
	}

}
