package ru.ifmo.cs.semnet.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Класс, описывающий структуру данных семантической сети.
 * 
 * *** FIXME	select - прИскорбно тупой (не работает с ссылками, 
 * 		поддеревьями и параметрами узлов, на которые ссылается)
 * 				remove - удаляет только через поиск. Добавить 
 * 		удаление объекта напрямую: remove(myNode);
 * 				insert - нужно больше параметров; нет проверки, 
 * 		что узел уже есть и его не надо добавлять
 * 
 * *** FIXME добавить методы неточного поиска 
 * 				и сохранения/чтения сети в/из файла
 * 
 * @author alex
 *
 * @param <T> - тип узлов с которыми будет работать сеть.
 * 			Generic, чтобы можно было расширить возможности
 * 			собственных узлов или просто переопределить операции
 * 			дефолтных ru.ifmo.cs.semnet.core.Node .
 */
public class SemanticNetwork<T extends Node> implements Serializable {
	
	private static final long serialVersionUID = -4230774241093159745L;

	/* пул потоков для организации процессов сети */
	private ExecutorService threadsPool = null;
	
	/* корневой узел сети */
	private final T rootNode;
	
	/**
	 * Инициализация семантической сети
	 * @param root - корневой узел (не может быть null)
	 */
	public SemanticNetwork(T root) {
		if(root == null) {
			throw new IllegalArgumentException("root node must be initialize");
		}
		rootNode = root;
		/* количество тредов сделать настраиваемым */
		threadsPool = Executors.newFixedThreadPool(20);
	}
	
	/**
	 * Чтение корневого узла сети. *Readonly*
	 * @return - корневой узел сети
	 */
	public T getRootNode() {
		return rootNode;
	}
	
	/* ********************************************************* */
	/* ***** SELECT'ы ИЗ СЕТИ и все что для них необходимо ***** */
	/* ********************************************************* */
	
	/* набор выбранных значений */
	private List<T> resultSelect;
	
	/* флаг: выборка множества значений, или первое найденное */
	private boolean multySelect;
	
	/**
	 * Выборка узла сети по заданным параметрам
	 * @param selector - объект, инкапсулирующий
	 * 			параметры выборки узлов из сети.
	 * @return - найденный узел, либо null
	 */
	public T select(Selector selector) {
		multySelect = false;
		selectProcess(selector);
		T t = null;
		if(resultSelect.size() > 0) {
			t = resultSelect.get(0);
		}
		return t;
	}
	
	/**
	 * Выборка набора узлов по заданным параметрам
	 * @param selector - объект, инкапсулирующий
	 * 			параметры выборки узлов из сети.
	 * @return - набор найденных узлов
	 */
	public Collection<T> selectAll(Selector selector) {
		multySelect = true;
		selectProcess(selector);
		return resultSelect;
	}
	
	/**
	 * Процесс выборки искомых значений
	 * @param selector
	 */
	@SuppressWarnings("unchecked")
	protected void selectProcess(Selector selector) {
		
		// "сисурити" структуры результатов
		resultSelect = Collections.synchronizedList(new ArrayList<>());
		
		// состояния потоков
		ArrayList<Future<?>> futures = new ArrayList<>();
		
		Node n = checkNodeOnSelect(rootNode, selector);
		if(n != null) {
			resultSelect.add((T) n);
			if(!multySelect) {
				return;
			}
		}
		
		for(Node childNode : rootNode.getChildNodes()) {
			
			// каждая дочерняя ветка порождает поток поиска
			// тут будет проблема при сильно большой разнице
			//   количества узлов в дочерних подеревьях
			SelectorThread thread = new SelectorThread(childNode, selector, (Node res) -> {
				resultHandler((T) res);
			}, multySelect);
			futures.add(threadsPool.submit(thread));
		}
		
		// ждем пока потоки ищут...
		while(true) {
			boolean allDone = true;
			for(Future<?> f : futures) {
				if(!f.isDone()) {
					allDone = false;
				}
			}
			
			// выходим, когда завершен поиск
			if(allDone) {
				break;
			}
		}
	}
	
	/**
	 * функция, которая обрабатывает реакцию на нахожение
	 * искомого узла при операциях выборки
	 * @param node - найденный узел
	 */
	protected void resultHandler(T node) {
		// если ожидается один узел в качестве результата
		// и этот узел уже был добавлен
		if(!multySelect && resultSelect.size() > 0) {
			return;
		}
		resultSelect.add(node);
	}

	/**
	 * Метод анализирует заданный узел на соответствие 
	 * заданному селектору. Если все параметры селектора
	 * найдены в узле и их значения равны искомым, то
	 * узел считается соответствующим селектору.
	 * @param node - анализируемый узел
	 * @param sel - заданный селектор
	 * @return - узел, если он соответствует, иначе null
	 */
	public static Node checkNodeOnSelect(Node node, Selector sel) {
		
		if(node == null || sel == null) {
			throw new IllegalArgumentException();
		}
		
		// флаг, что все параматры выборки совпадают
		boolean searched = true;
		
		// перебор по всем искомым параметрам
		for(String p : sel.getSelectParams().keySet()) {
			try {
				Object pValue = node.selectParam(sel.getLocale(), p);
				
				// если параметр не существует или не равен тому, что в селекторе
				if(pValue == null || !pValue.equals(sel.getSelectParams().get(p))) {
					return null;
				}
			} catch (LangNotSupportedException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		return searched ? node: null;
	}
	
	/* ********************************************************* */
	/* ***** REMOV'ы ИЗ СЕТИ и все что для них необходимо ****** */
	/* ********************************************************* */
	
	/**
	 * Удаление узла из сети
	 * 
	 * @param selector - объект, который "расскажет", как найти того, кого хотим удалить
	 * @param opts - объект, который "расскажет", как будет склеивать разрыв в сети
	 * @return - успешно ли прошла операция
	 */
	public boolean remove(Selector selector, ModifyOptions opts) {
		
		// ищем, что будем удалять
		T removeNode = select(selector);
		if(removeNode == null) {
			return false;
		}
		
		// получаем все поддеревья удаляемого узла
		Collection<Node> children = removeNode.getChildNodes();
		
		// если задана опция присоединения поддеревьев
		if(opts.isConcatenateChildrenToRoot() && children.size() > 0) {
			for(Node child : children) {
				rootNode.addChild(child);
			}
		} else if(opts.isConcatenateChildrenToParent() && children.size() > 0) {
			Node parent = removeNode.getParent();
			if(parent != null) {
				for(Node n : children) {
					parent.addChild(n);
				}
				
			parent.removeChild(removeNode.getDefaultView(), removeNode.getDefaultLocale());
			
			} else {
				return false;
			}
		}
		
		// отключение ноды от сети
		// ПЕЧАЛЬ: могут остаться ссылки в других узлах 
		// и GC не почистит все это дело =(
		// подумать, что с этим сделать
		removeNode.getLinks().clear();
		removeNode.getParams().clear();
		
		// ну это чисто поржать =)
		removeNode = null;
		
		return true;
	}
	
	
	/**
	 * Расчет размера сети
	 * @return - кол-во узлов сети
	 */
	public long size() {
		// тяжеловато будет...
		// надо навесить кеш где нибудь снаружи
		return rootNode.getCountChild() + 1;
	}
	
	/* ********************************************************* */
	/* ****** INSERT'ы В СЕТь и все что для них необходимо ***** */
	/* ********************************************************* */
	
	/**
	 * Вставка нового узла в сеть
	 * @param next - вставляемый узел
	 * @param options - объект, который "расскажет", как 
	 * 		будет разрешать ссылки при встраивании в сеть
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T insert(Node next, ModifyOptions options) {
		
		if(next == null) {
			return null;
		}
		
		if(options.isReplaceWithRoot()) {
			// замена рутовой ноды выпилена
			return rootNode;
		}
		
		if(options.isChildForNode() && options.getKeyNode() != null) {
			
			if(options.isMoveChild()) {
				Collection<Node> children = options.getKeyNode().getChildNodes();
				for(Node n : children) {
					next.addChild(n);
					options.getKeyNode().removeChild(n.getDefaultView(), n.getDefaultLocale());
				}
			}
			
			options.getKeyNode().addChild(next);
			return (T) next;
		}
		return null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		threadsPool.shutdownNow();
		super.finalize();
	}
}
