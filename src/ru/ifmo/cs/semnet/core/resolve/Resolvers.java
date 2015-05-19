package ru.ifmo.cs.semnet.core.resolve;

import java.util.List;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;
import ru.ifmo.cs.semnet.core.select.SelectBuilder;

public class Resolvers {
	
	public static <T extends Node> InsertAsNewParentResolver<T> 
				createAsNewParentResolver(SemanticNetwork<T> semNet, String parentView) {
		List<T> list = semNet.select(SelectBuilder.select()
				.param(SemNetUtils.VIEW_NAME_PARAMETER, parentView).build());
		if(list != null && list.size() > 0) {
			return new InsertAsNewParentResolver<>(list.get(0).getId());
		}
		return null;
	}
	
	public static <T extends Node> InsertChildLinkResolver<T>
				createChildInsertResolver(SemanticNetwork<T> semNet, String parentView) {
		List<T> list = semNet.select(SelectBuilder.select()
				.param(SemNetUtils.VIEW_NAME_PARAMETER, parentView).build());
		if(list != null && list.size() > 0) {
			return new InsertChildLinkResolver<>(list.get(0).getId());
		}
		return null;
	}

}
