package ru.ifmo.cs.semnet.core.resolve;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;

/**
 * "Решатель конфликтов" связей при модификации сети алгоритмом вставки со 
 * смещением дочерних элементов. Алгоритм заключется в том, что внедряемому 
 * узлу присваивается родительский узел и все ссылки на дочерние элементы,
 * которые заданы в резолвере перемещяются новому узлу. Т.е.:
 * 
 * ( 1 -> [3, 4, 5] ).insert(1, [3, 4]) => 1 -> [2 -> [3, 4], 5]
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 *
 * @param <T>
 */
public class InsertWithMoveChildResolver<T extends Node> implements LinkResolver<T> {

	/* construct injection */
	private long parent;
	
	/* construct injection */
	private Long[] moveChilds;
	
	public InsertWithMoveChildResolver(long idParent, Long ...childIds) {
		parent = idParent;
		moveChilds = childIds;
	}

	@Override
	public void resolve(T modifySource, Map<Long, ? extends T> storage) {
		Node parentNode = storage.get(parent);
		
		List<Long> pNodes = parentNode.getChilds().stream().map(n -> n.getId()).collect(Collectors.toList());
		if(moveChilds != null) {
			for(long id : moveChilds) {
				if(pNodes.contains(id)) {
					modifySource.addChild(id);
					parentNode.removeChild(id);
				}
			}
		}
		parentNode.addChild(modifySource.getId());
	}
}
