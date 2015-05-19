package ru.ifmo.cs.semnet.core.resolve;

import java.util.List;
import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.TypeLink;

/**
 * "Решатель конфликтов" связей при модификации сети алгоритмом вставки нового родителя.
 * Алгоритм заключется в том, что внедряемому узлу присваивается родительский узел у 
 * которого все ссылки на дочерние узлы отбираются новым узлом. Т.е.:
 * 
 * ( 1 -> [3, 4] ).insert(1) => 1 -> 2 -> [3, 4]
 * аргумент - родительский узел внедряемого узла
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 * 
 * @param <T>
 */
public class InsertAsNewParentResolver<T extends Node> implements
        LinkResolver<T> {

	/* constructor injection */
    private long parentNodeId;

    /**
     * @param parentId идентификатор родительского узла
     */
    public InsertAsNewParentResolver(long parentId) {
        this.parentNodeId = parentId;
    }

    @Override
    public void resolve(T modifySource, Map<Long, ? extends T> storage) {
        Node parent = storage.get(parentNodeId);
        List<Node> list = parent.getChilds();
        parent.removeLinks(TypeLink.CHILD);
        parent.addChild(modifySource.getId());
        for (Node n : list) {
            modifySource.addChild(n.getId());
        }
    }

}