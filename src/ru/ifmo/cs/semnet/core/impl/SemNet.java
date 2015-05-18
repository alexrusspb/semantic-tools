package ru.ifmo.cs.semnet.core.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import ru.ifmo.cs.semnet.core.Finder;
import ru.ifmo.cs.semnet.core.LinkResolver;
import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.SemanticNetwork;
import ru.ifmo.cs.semnet.core.exception.FailInitSemanticNetworkException;

/**
 * Реализация семантической сети на узлах по умолчанию
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class SemNet implements SemanticNetwork<DefaultNode> {

	/* Физическая структура данных семантической сети */
	private Map<Long, DefaultNode> storage = null;
	
	/* присваеваемый ид новым узлам (инкрементальный) */
	private static long nextId = 1;
	
	/**
	 * Инициализация сети из файла
	 * 
	 * @param file файл с сериализованной сетью
	 */
	public SemNet(File file) {
		try {
			restore(file);
		} catch (Exception ex) {
			ex.printStackTrace();
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
    public boolean remove(Selector selector,
            LinkResolver<? super DefaultNode> resolver) {
        List<DefaultNode> removedNodes = select(selector);
        if (removedNodes.size() > 0) {
            for (DefaultNode n : removedNodes) {
                resolver.resolve(n, storage);
                n.onRemoveNode();
                storage.remove(n);
            }
            return true;
        }
        return false;
    }

    @Override
    public DefaultNode insert(String view, 
    		LinkResolver<? super DefaultNode> resolver) {
        return insert(view, Locale.getDefault(), resolver);
    }

    @Override
    public DefaultNode insert(String view, Locale locale,
            LinkResolver<? super DefaultNode> resolver) {
        DefaultNode newNode = new DefaultNode(storage, view, nextId++, locale);
        storage.put(newNode.getId(), (DefaultNode) newNode);
        resolver.resolve(newNode, storage);
        return newNode;
    }

	@Override
	public List<DefaultNode> find(Finder find) {
		return select(find);
	}

	@Override
	public void save(File file) throws IOException {
		try {
			OutputStream fs = new FileOutputStream(file);
			OutputStream buffer = new BufferedOutputStream(fs);
			ObjectOutput output = new ObjectOutputStream(buffer);
			
			output.writeObject(storage);
			
			output.flush();
			output.close();
		} catch(IOException exept) {
			System.out.println("печаль при сохранении параметров");
			exept.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void restore(File file) throws IOException {
		try {
			InputStream fs = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(fs);
			ObjectInput in = new ObjectInputStream(buffer);
			
			storage = (ConcurrentHashMap<Long, DefaultNode>) in.readObject();
			in.close();
			// "вспоминаем" кто был последний
			nextId = storage.keySet().parallelStream().mapToLong(l -> l).max().getAsLong() + 1;
		} catch(ClassNotFoundException exept) {
			System.out.println("Неудача при восстановлении сети из файла");
			exept.printStackTrace();
		}
	}

	@Override
	public long sizeNetwork() {
		return storage.size();
	}

}
