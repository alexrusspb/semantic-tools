package ru.ifmo.cs.semnet.core.resolve;

import java.util.List;
import java.util.Map;

import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.TypeLink;

public class InsertAsNewParentResolver<T extends Node> implements
        LinkResolver<T> {

    private long parentNodeId;


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