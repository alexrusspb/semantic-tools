package ru.ifmo.cs.semnet.core.resolve;

import java.util.List;
import java.util.stream.Collectors;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.impl.SemNetUtils;
import ru.ifmo.cs.semnet.core.select.SelectBuilder;

/**
 * Фабрика резолверов связей
 * 
 * @author Pismak Alexey
 * @lastUpdate 19 мая 2015 г.
 */
public class Resolvers {
	
	/**
	 * Создает экземпляр резолвера, который решает конфликты 
	 * связей при вставке  элемента  алгоритмом  AsNewParent
	 * Подробнее об этом в {@link=InsertAsNewParentResolver}
	 * 
	 * @param semNet сеть в которую будет произведена вставка
	 * @param parentView представление родительского узла
	 * @return экземпряр резолвера
	 */
	public static <T extends Node> InsertAsNewParentResolver<T> 
				createAsNewParentResolver(SemanticNetwork<T> semNet, String parentView) {
		List<T> list = semNet.select(SelectBuilder.select()
				.param(SemNetUtils.VIEW_NAME_PARAMETER, parentView).build());
		if(list != null && list.size() > 0) {
			return new InsertAsNewParentResolver<>(list.get(0).getId());
		}
		return null;
	}
	
	/**
	 * Создает экземпляр резолвера, который решает конфликты 
	 * связей при вставке  элемента  алгоритмом NewChildNode
	 * Подробнее об этом в {@link=InsertChildLinkResolver}
	 * 
	 * @param semNet сеть в которую будет произведена вставка
	 * @param parentView представление родительского узла
	 * @return экземпряр резолвера
	 */
	public static <T extends Node> InsertChildLinkResolver<T>
				createChildInsertResolver(SemanticNetwork<T> semNet, String parentView) {
		List<T> list = semNet.select(SelectBuilder.select()
				.param(SemNetUtils.VIEW_NAME_PARAMETER, parentView).build());
		if(list != null && list.size() > 0) {
			return new InsertChildLinkResolver<>(list.get(0).getId());
		}
		return null;
	}

	/**
	 * Создает экземпляр резолвера, который решает конфликты 
	 * связей при вставке  элемента  алгоритмом BetweenNodes
	 * Подробнее об этом в {@link=InsertBetweenLinkResolver}
	 * 
	 * @param semNet сеть в которую будет произведена вставка
	 * @param parentView представление родительского узла
	 * @param childView представление дочернего узла
	 * @return экземпряр резолвера
	 */
	public static <T extends Node> InsertBetweenLinkResolver<T>
				createInsertBetweenNodes(SemanticNetwork<T> semNet, 
						String parentView, String childView) {
		// при необходимости оптимизировать, выбрав родительский идочерний узлы одним select'ом
		List<T> list = semNet.select(SelectBuilder.select()
				.param(SemNetUtils.VIEW_NAME_PARAMETER, parentView).build());
		if(list != null && list.size() > 0) {
			T parent = list.get(0);
			list = semNet.select(SelectBuilder.select()
					.param(SemNetUtils.VIEW_NAME_PARAMETER, childView).build());
			if(list != null && list.size() > 0) {
				T child = list.get(0);
				return new InsertBetweenLinkResolver<>(parent.getId(), child.getId());
			}
		}
		return null;
	}
	
	/**
	 * Создает экземпляр резолвера, который решает конфликты 
	 * связей при вставке  элемента  алгоритмом MoveMeChilds
	 * Подробнее об этом в {@link=InsertWithMoveChildResolver}
	 * 
	 * @param sn сеть в которую будет произведена вставка
	 * @param parentView представление родительского узла
	 * @param child представлениея дочерних узлов
	 * @return экземпряр резолвера
	 */
	public static <T extends Node> InsertWithMoveChildResolver<T>
				createInsertWithMoveChildResolver(SemanticNetwork<T> sn, String parentView, 
						String ...child) {
		List<T> list = sn.select(SelectBuilder.select()
				.param(SemNetUtils.VIEW_NAME_PARAMETER, parentView).build());
		if(list != null && list.size() > 0) {
			T parent = list.get(0);
			list.clear();
			if(child != null && child.length > 0) {
				for(String s : child) {
					List<T> ch = sn.select(SelectBuilder.select()
							.param(SemNetUtils.VIEW_NAME_PARAMETER, s).build());
					if(ch != null && ch.size() > 0) {
						list.add(ch.get(0));
					}
				}
				if(!list.isEmpty()) {
					Long[] chArgs = new Long[list.size()];
					chArgs = list.stream().map(n -> n.getId()).collect(Collectors.toList()).toArray(chArgs);
					return new InsertWithMoveChildResolver<>(parent.getId(), chArgs);
				}
				return new InsertWithMoveChildResolver<>(parent.getId());
			}
		}
		return null;
	}
	
	/**
	 * Создает экземпляр резолвера, который решает конфликты 
	 * связей при вставке  элемента  алгоритмом NewChildNode
	 * Подробнее об этом в {@link=InsertChildLinkResolver}
	 * 
	 * @param semNet сеть в которую будет произведена вставка
	 * @param selectParent объект, задающий критерии поиска родительского узла
	 * @return экземпляр резолвера
	 */
	public static <T extends Node> InsertChildLinkResolver<T> 
				createChildInsertResolver(SemanticNetwork<T> semNet, Selector selectParent) {
		List<T> list = semNet.select(selectParent);
		if(list != null && list.size() > 0) {
			return new InsertChildLinkResolver<>(list.get(0).getId());
		}
		return null;
	}
	
	/**
	 * @return резолвер простого удаления узла
	 */
	public static <T extends Node> EasyRemoveLinkResolver<T> createEasyRemoveResolver() {
		return new EasyRemoveLinkResolver<>();
	}
	
	/**
	 * Создает экземпляр резолвера, который решает конфликты 
	 * связей при вставке  элемента  алгоритмом BetweenNodes
	 * Подробнее об этом в {@link=InsertBetweenLinkResolver}
	 * 
	 * @param parentId идентификатор родительского узла
	 * @param childId идентификатор дочернего узла
	 * @return экземпляр резолвера
	 */
	public static <T extends Node> InsertBetweenLinkResolver<T> 
				createInsertBetweenResolver(long parentId, long childId) {
		return new InsertBetweenLinkResolver<>(parentId, childId);
	}
}
