package ru.ifmo.cs.semnet.core.resolve;

import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;

/**
 * "Решатель конфликтов" связей при модификации сети алгоритмом простого удаления.
 * Алгоритм заключется в том, что все дочерние ссылки удаляемого узла на дочерние 
 * узлы перемещаются родителю удалаемого узла. Т.е.:
 * 
 * ( 1 -> 2 -> [3, 4] ).remove(2) => 1 -> [3, 4]
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 * 
 * @param <T> тип узлов с которыми работает резолвер
 */
public class EasyRemoveLinkResolver<T extends Node> implements LinkResolver<T> {

	@Override
	public void resolve(T modifySource, Map<Long, ? extends T> storage) {
		Node node = modifySource.getParentNode();
		if(node != null) {
			for( Node n : modifySource.getChilds()) {
				n.changeParent(node.getId());
			}
			node.removeChild(modifySource.getId());
		}
	}
}
