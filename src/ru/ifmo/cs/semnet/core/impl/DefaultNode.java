package ru.ifmo.cs.semnet.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.TypeLink;
import ru.ifmo.cs.semnet.core.exeption.LangNotSupportedException;
import ru.ifmo.cs.semnet.core.exeption.MutatedNodeException;
import ru.ifmo.cs.semnet.core.impl.utils.SemNetUtils;

/**
 * Реализация узла сети, используемая в ВКР как тип узлов по умолчанию
 * 
 * @lastUpdate 17 мая 2015 г.
 * @author Pismak Alexey
 */
public class DefaultNode implements Node {

	private static final long serialVersionUID = 2641191719375895103L;

	/*
	 * Набор связей узла формируют логическую структуру
	 * данных семантической сети.  Физическая структура
	 * определяется  как  простой  единый  набор  узлов. 
	 */
	
	/* ссылка на объект, где хранятся ВСЕ остальные узлы текущей сем. сети */
	private Map<Long, ? extends Node> storage;
	
	/* Набор связей узла */
	private Map<Long, TypeLink> links;
	
	/* Набор параметров узла */
	private Map<Locale, Map<String, Object>> params;
	
	/* идентификатор узла в сети */
	private final long ident;
	
	private final Locale initLocale;
	
	/**
	 * Инициализация узла сети
	 * 
	 * @param linkToSemNet ссылка на сеть в которой узел будет находиться
	 * @param view представление понятия
	 * @param id уникальный номер понятия
	 */
	public DefaultNode(Map<Long, ? extends Node> linkToSemNet, String view, long id) {
		this(linkToSemNet, view, id, null);
	}
	
	/**
	 * Инициализация узла сети
	 * 
	 * @param linkToSemNet ссылка на сеть в которой узел будет находиться
	 * @param view представление понятия
	 * @param id уникальный номер понятия
	 * @param locale для какой локали задано представление. может быть 
	 * 			<code>null</code>, тогда используется локаль по умолчанию
	 */
	public DefaultNode(Map<Long, ? extends Node> linkToSemNet, String view, long id, Locale locale) {
		storage = linkToSemNet;
		ident = id;
		links = new HashMap<>();
		params = new HashMap<>();
		initLocale = locale == null ? Locale.getDefault() : locale;
		addParameter(SemNetUtils.VIEW_NAME_PARAMETER, view, initLocale);
	}

	@Override
	public long getId() {
		return ident;
	}

	@Override
	public String getView() {
		String result = null;
		
		try {
			result = getView(initLocale);
		} catch (LangNotSupportedException ex) {
			List<Object> views = params.values().stream()
					.map(k -> k.get(SemNetUtils.VIEW_NAME_PARAMETER)).collect(Collectors.toList());
			if(views != null && views.size() > 0) {
				result = views.get(0).toString();
			} else {
				throw new MutatedNodeException(this, "View parameter was deleted");
			}
		}
		return result;
	}

	@Override
	public String getView(Locale locale) throws LangNotSupportedException {
		String result = null;
		if(params.containsKey(locale)) {
			Map<String, Object> map = params.get(locale);
			if(map.containsKey(SemNetUtils.VIEW_NAME_PARAMETER)) {
				result = map.get(SemNetUtils.VIEW_NAME_PARAMETER).toString();
			}
		} else {
			throw new LangNotSupportedException(locale, this);
		}
		return result;
	}

	@Override
	public List<Node> getChilds() {
		return getLinks(TypeLink.CHILD);
	}

	@Override
	public List<Node> getLinks(TypeLink type) {
		if(type == null) {
			return Collections.emptyList();
		}
		/* берем все ссылки -> фильтруем по типу -> конвертируем полученное в список  */
		return links.keySet().stream().filter(id -> links.get(id).equals(type))
				.map(id -> storage.get(id)).collect(Collectors.toList());
	}
	
	@Override
	public Map<Long, TypeLink> getLinks() {
		return links;
	}

	// FIXME добавить обратное линкование: a -> b => b -> a
	@Override
	public boolean addLink(long idNode, TypeLink type) {
		// если узел уже ссылается на узел с таким id
		if(links.containsKey(idNode) || type == null) {
			return false;
		}
		// два уникальных случая
		if(type.equals(TypeLink.CHILD)) {
			return addChild(idNode);
		}
		if(type.equals(TypeLink.PARENT)) {
			return changeParent(idNode);
		}
		// остальные типы ссылок просто добавляем
		links.put(idNode, type);
		return true;
	}

	@Override
	public boolean removeLink(long idNode) {
		if(!links.containsKey(idNode)) {
			return false;
		}
		TypeLink type = links.get(idNode);
		if(type.equals(TypeLink.PARENT)) {
			return false;
		}
		if(type.equals(TypeLink.CHILD)) {
			return removeChild(idNode);
		}
		links.remove(idNode);
		return true;
	}

	@Override
	public boolean removeLinks(TypeLink type) {
		if(type == null) {
			return false;
		}
		// получаем id всех удаляемых нод
		List<Long> removedNodes = getLinks(type).stream().map(n -> n.getId()).collect(Collectors.toList());
		if(removedNodes.size() > 0) {
			// очищаем ссылки
			removedNodes.forEach(l -> {
				links.remove(l);
			});
			return true;
		}
		return false;
	}

	@Override
	public void clearLinks() {
		for(TypeLink type : TypeLink.values()) {
			// зачистка всех ссылок, кроме родителя
			if(!type.equals(TypeLink.PARENT)) {
				removeLinks(type);
			}
		}
	}

	@Override
	public void onRemoveNode() {
		links.clear();
		links = null;
		params.values().forEach(map -> {
			map.clear();
		});
		params.clear();
		params = null;
	}

	@Override
	public boolean addChild(long idNode) {
		if(links.containsKey(idNode)) {
			return false;
		}
		// перевязываем ссылками
		Node childNode = storage.get(idNode);
		// добаляемая ссылка должна уже находиться в физической
		// структуре данных семантической сети 
		if(childNode == null) {
			throw new MutatedNodeException(this, "attempt add child, "
				+ "which not found in nodes storage [CHILD_ID = " + idNode + "]");
		}
		childNode.changeParent(getId());
		links.put(idNode, TypeLink.CHILD);
		return true;
	}

	@Override
	public boolean removeChild(long idChild) {
		if(links.containsKey(idChild) && links.get(idChild).equals(TypeLink.CHILD)) {
			Node node = storage.get(idChild);
			if(node == null) {
				throw new MutatedNodeException(this, "attempt remove child, "
					+ "which not found in nodes storage [CHILD_ID = " + idChild + "]");
			}
			if(node.changeParent(SemNetUtils.ROOT_NODE_ID)) {
				links.remove(idChild);
			}
		}
		return false;
	}

	@Override
	public Node getParentNode() {
		List<Node> parents = getLinks(TypeLink.PARENT);
		if(parents.size() > 1) {
			throw new MutatedNodeException(this, "Node has more one parent link");
		}
		return parents.size() > 0 ? parents.get(0) : null;
	}

	@Override
	public boolean changeParent(long newParentId) {
		if(!storage.containsKey(newParentId)) {
			return false;
		}
		Node parent = getParentNode();
		if(parent != null) {
			links.remove(parent.getId());
		}
		links.put(newParentId, TypeLink.PARENT);
		return true;
	}

	@Override
	public Object getValueLocalizedParameter(String parameterName, Locale locale)
			throws LangNotSupportedException {
		if(!params.containsKey(locale)) {
			throw new LangNotSupportedException(locale, this);
		}
		return params.get(locale).get(parameterName);
	}

	@Override
	public String getNameLocalizedParameter(Object parameterValue, Locale locale)
			throws LangNotSupportedException {
		if(!params.containsKey(locale)) {
			throw new LangNotSupportedException(locale, this);
		}
		/* Находим все имена параметров, которые имеют заданное значение */
		List<String> list = params.get(locale).keySet().stream()
				.filter(s -> params.get(locale).get(s).equals(parameterValue))
				.collect(Collectors.toList());
		 return (list == null || list.isEmpty()) ? null : list.get(0);
	}

	@Override
	public Map<String, Object> getAllLocalizedParameters(Locale locale)
			throws LangNotSupportedException {
		if(isSupportedLocale(locale)) {
			return params.get(locale);
		}
		throw new LangNotSupportedException(locale, this);
	}

	@Override
	public Map<String, List<Object>> getAllParameters() {
		Map<String, List<Object>> result = new HashMap<>();
		for(Map<String, Object> map : params.values()) {
			for(String key : map.keySet()) {
				if(!result.containsKey(key)) {
					result.put(key, new ArrayList<>());
				}
				result.get(key).add(map.get(key));
			}
		}
		return result;
	}
	
	@Override
	public List<Locale> getSupportedLocales() {
		return params.keySet().stream().collect(Collectors.toList());
	}

	@Override
	public boolean isSupportedLocale(Locale locale) {
		return params.containsKey(locale);
	}

	@Override
	public Object getParameterByName(String name) {
		for(Locale locale : params.keySet()) {
			Map<String, Object> map = params.get(locale);
			/* собираем все параметры по имени в текущей локали */
			List<Object> list = map.keySet().stream().filter(n -> n.equals(name))
					.map(s -> map.get(s)).collect(Collectors.toList());
			/* если хоть оди найден, то возвращаем его */
			if(list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	@Override
	public List<Object> getAllParametersByName(String name) {
		Map<String, Object> map = new HashMap<>();
		/* все параметры узла складываем в один набор */
		for(Locale locale : params.keySet()) {
			map.putAll(params.get(locale));
		}
		/* фильтруем по именам параметров и возвращяем оставшиеся values */
		return map.keySet().stream().filter(n -> n.equals(name))
				.map(s -> map.get(s)).collect(Collectors.toList());
	}

	@Override
	public boolean addParameter(String name, Object value) {
		return addParameter(name, value, initLocale);
	}

	@Override
	public boolean addParameter(String name, Object value, Locale locale) {
		Map<String, Object> localizedParams = null;
		if(!params.containsKey(locale)) {
			localizedParams = new HashMap<>();
			params.put(locale, localizedParams);
		} else {
			localizedParams = params.get(locale);
		}
		/* если параметр уже есть, то не добавляем */
		if(!localizedParams.containsKey(name)) {
			localizedParams.put(name, value);
			return true;
		}
		return false;
	}

	@Override
	public void removeParameter(String name) {
		/* параметры представления являются служебными и не удаляются */
		if(name != SemNetUtils.VIEW_NAME_PARAMETER) {
			for(Locale l : params.keySet()) {
				Map<String, Object> map = params.get(l);
				for(String s : map.keySet()) {
					if(s.equals(name)) {
						map.remove(s);
					}
				}
			}
		}
	}

	@Override
	public Object removeLocalizedParameter(String name, Locale locale)
			throws LangNotSupportedException {
		if(!params.containsKey(locale)) {
			throw new LangNotSupportedException(locale, this);
		}
		Map<String, Object> map = params.get(locale);
		if(map.containsKey(name)) {
			return map.remove(name);
		}
		return null;
	}

	@Override
	public void removeLocale(Locale locale) throws LangNotSupportedException {
		if(!params.containsKey(locale)) {
			throw new LangNotSupportedException(locale, this);
		}
		/* если попытка удаления "служебной локали" */
		if(locale.equals(initLocale)) {
			/* то удаляем все параметры локали, кроме представления */
			Map<String, Object> map = params.get(locale);
			for(String s : map.keySet()) {
				if(s.equals(SemNetUtils.VIEW_NAME_PARAMETER)) {
					continue;
				}
				map.remove(s);
			}
			return;
		}
		/* иначе удаляем все параметры локали */
		params.remove(locale).clear();
	}

	@Override
	public void modifyParameters(String name, Object value, Object newValue) {
		for(Locale locale : getSupportedLocales()) {
			try {
				modifyLocalizedParameter(name, value, newValue, locale);
			} catch (LangNotSupportedException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public boolean modifyLocalizedParameter(String name, Object value,
			Object newValue, Locale locale) throws LangNotSupportedException {
		if(!params.containsKey(locale)) {
			throw new LangNotSupportedException(locale, this);
		}
		Map<String, Object> map = params.get(locale);
		if(name != null) {
			if(map.containsKey(name)) {
				if(map.get(name).equals(value)) {
					return map.replace(name, value, newValue);
				}
			}
		}
		return false;
	}

	@Override
	public String toVerboseString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NODE [");
		sb.append(getId());
		sb.append("]\n");
		for(Locale l : params.keySet()) {
			sb.append("PARAMETERS FOR LOCALE [ ");
			sb.append(l.toString());
			sb.append(" ]:");
			for(String param : params.get(l).keySet()) {
				sb.append("\n  [");
				sb.append(param);
				sb.append("] -> ");
				sb.append(tryWrapperGetterParameter(param, l));
			}
			sb.append("\n");
		}
		sb.append("LINKAGE SET");
		for(Long key : links.keySet()) {
			sb.append("\n  [связь: ");
			sb.append(links.get(key));
			sb.append("]: -> [ ID: \"");
			sb.append(key);
			sb.append("\"]");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return getView();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ident ^ (ident >>> 32));
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return ident == ((DefaultNode) obj).ident;
	}
	
	/* Чтение значения локализованного параметра с оберткой try-catch */
	private Object tryWrapperGetterParameter(String paramName, Locale locale) {
		try {
			return getValueLocalizedParameter(paramName, locale);
		} catch (LangNotSupportedException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
