package ru.ifmo.cs.semnet.core.resolve;

import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;

/**
 * "Решатель конфликтов" связей при модификации сети алгоритмом вставки нового узла.
 * Простой алгоритм заключется в том, что внедряемому узлу присваивается ссылка на 
 * родительский узел. Т.е.:
 * 
 * ( 1 -> 2 ).insert(1) => ( 1 ->  [2, 3])
 * аргумент - родительский узел внедряемого узла
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 * 
 * @param <T>
 */
public class InsertChildLinkResolver<T extends Node> implements LinkResolver<T> {

    private static final long NOT_INIT = -1L;

    private long parentNodeId = NOT_INIT;

    public InsertChildLinkResolver(long idParent) {
        this.parentNodeId = idParent;
    }

    @Override
    public void resolve(T modifySource, Map<Long, ? extends T> storage) {
        T parent = storage.get(parentNodeId);
        parent.addChild(modifySource.getId());
    }
}