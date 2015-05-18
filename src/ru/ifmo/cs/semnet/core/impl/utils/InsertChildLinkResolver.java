package ru.ifmo.cs.semnet.core.impl.utils;

import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.SemanticNetwork;

public class InsertChildLinkResolver implements LinkResolver {

	private static final long NOT_INIT = -1L;
	
	private long parentNodeId = NOT_INIT;
	
	private Selector selector;
	
	public InsertChildLinkResolver(long idParent) {
		this.parentNodeId = idParent;
	}
	
	/**
	 * warning (!)  O(n). Use InsertChildLinkResolver(long idParent);
	 * @param parentSelector
	 */
	public InsertChildLinkResolver(Selector parentSelector) {
		if(parentSelector == null) {
			throw new IllegalArgumentException("Selector in resolver can not be null");
		}
		selector = parentSelector;
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
		parent.addChild(modifySource.getId());
		
		return (T)modifySource;
	}


}
