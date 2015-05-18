package ru.ifmo.cs.semnet.core.resolve;

import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;

public class EasyRemoveLinkResolver<T extends Node> implements LinkResolver<T> {

	@Override
	public void resolve(T modifySource, Map<Long, ? extends T> storage) {
		
	}
}
