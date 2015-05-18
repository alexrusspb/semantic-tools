package ru.ifmo.cs.semnet.core.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ru.ifmo.cs.semnet.core.Finder;
import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.exeption.FailInitSemanticNetworkException;
import ru.ifmo.cs.semnet.core.impl.utils.SemNetUtils;

public class SemNet implements SemanticNetwork<DefaultNode> {

	private Map<Long, DefaultNode> storage = null;
	
	private static long nextId = 1;
	
	public SemNet(File file) {
		try {
			restore(file);
		} catch (Exception ex) {
			throw new FailInitSemanticNetworkException(ex);
		}
	}
	
	/**
	 * Инициализация семантической сети ее корневым узлом.
	 * 
	 * @param viewRoot представление корневого узла. Не может быть null.
	 * @param localeViewRoot локаль представления. Может быть null,
	 * 				тогда используется локаль по умолчанию
	 */
	public SemNet(String viewRoot, Locale localeViewRoot) {
		storage = new ConcurrentHashMap<>();
		DefaultNode root = new DefaultNode(storage, viewRoot, SemNetUtils.ROOT_NODE_ID, localeViewRoot);
		storage.put(root.getId(), root);
	}

	@Override
	public DefaultNode getRootNode() {
		return storage.keySet().parallelStream()
				.filter(id -> id.equals(SemNetUtils.ROOT_NODE_ID))
				.map(id -> storage.get(id)).findFirst().get();
	}

	@Override
	public List<DefaultNode> select(Selector selector) {
		/*
		 * Достаем набор ключей и в параллельном стриме запускаем
		 * фильтрацию путем проверки узлов селектором (по ключу).
		 * Далее конвертируем в список, удобный для возвращаемого значения.
		 */
		return storage.keySet().parallelStream()
			.filter(key -> selector.checkNode(storage.get(key)))
			.map(key -> storage.get(key)).collect(Collectors.toList());
	}

	@Override
	public boolean remove(Selector selector, LinkResolver resolver) {
		List<DefaultNode> removedNodes = select(selector);
		if(removedNodes.size() > 0) {
			for(Node n : removedNodes) {
				resolver.resolve(n, storage, this);
				n.onRemoveNode();
				storage.remove(n).getId();
			}
		}
		return false;
	}

	@Override
	public DefaultNode insert(String view, LinkResolver resolver) {
		return insert(view, Locale.getDefault(), resolver);
	}

	@Override
	public DefaultNode insert(String view, Locale locale, LinkResolver resolver) {
		Node newNode = new DefaultNode(storage, view, nextId++, locale);
		storage.put(newNode.getId(), (DefaultNode)newNode);
		return resolver.resolve(newNode, storage, this);
	}

	@Override
	public List<DefaultNode> find(Finder find) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(File file) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restore(File file) throws IOException {
		// TODO Auto-generated method stub
		
		// "вспоминаем" кто был последний
		nextId = storage.keySet().parallelStream().mapToLong(l -> l).max().getAsLong() + 1;
	}

	@Override
	public long sizeNetwork() {
		return storage.size();
	}

}
