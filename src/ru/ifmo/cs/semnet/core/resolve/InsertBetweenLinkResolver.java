package ru.ifmo.cs.semnet.core.resolve;

import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;

/**
 * "Решатель конфликтов" связей при модификации сети алгоритмом вставки между 
 * заданными узлами. Алгоритм заключется в том, что внедряемому узлу присваивается 
 * родительский узел у которого забирается ссылка на указанный дочерний узел. Т.е.:
 * 
 * ( 1 -> [3, 4] ).insert(1, 3) => 1 -> [2 -> 3, 4]
 * 
 * Узел 2 вставлен между 1 и 3. 4 узел остался дочерним первому.
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 * 
 * @param <T>
 */
public class InsertBetweenLinkResolver<T extends Node> implements LinkResolver<T> {

	/* construct injection */
	private long parent;
	
	/* construct injection */
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
