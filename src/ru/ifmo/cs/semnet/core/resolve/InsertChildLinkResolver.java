package ru.ifmo.cs.semnet.core.resolve;

import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;

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