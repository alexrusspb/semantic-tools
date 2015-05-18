package ru.ifmo.cs.semnet.core.resolve;

import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;

public class InsertBetweenLinkResolver<T extends Node> implements LinkResolver<T> {

	private long parent;
	
	private long child;

	public InsertBetweenLinkResolver(long parentId, long childId) {
		parent = parentId;
		child = childId;
	}

	@Override
	public void resolve(T modifySource, Map<Long, ? extends T> storage) {
		Node pNode = storage.get(parent);
		Node cNode = storage.get(child);
		if(pNode.getChilds().contains(cNode)) {
			pNode.addChild(modifySource.getId());
			cNode.changeParent(modifySource.getId());
		}
	}
}
