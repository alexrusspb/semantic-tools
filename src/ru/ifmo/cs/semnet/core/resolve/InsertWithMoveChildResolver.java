package ru.ifmo.cs.semnet.core.resolve;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;

public class InsertWithMoveChildResolver<T extends Node> implements LinkResolver<T> {

	private long parent;
	
	private long[] moveChilds;
	
	public InsertWithMoveChildResolver(long idParent, long ...childIds) {
		parent = idParent;
		moveChilds = childIds;
	}

	@Override
	public void resolve(T modifySource, Map<Long, ? extends T> storage) {
		Node parentNode = storage.get(parent);
		
		List<Long> pNodes = parentNode.getChilds().stream().map(n -> n.getId()).collect(Collectors.toList());
		for(long id : moveChilds) {
			if(pNodes.contains(id)) {
				modifySource.addChild(id);
				parentNode.removeChild(id);
			}
		}
		parentNode.addChild(modifySource.getId());
	}
}
