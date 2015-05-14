package ru.ifmo.cs.semnet.core;

import java.util.ArrayList;

/**
 * Поток для осуществления поиска.
 * 
 * FIXME учитыват балансирование дерева либо переделать
 * алгоритм на тот, что указан в fixme в Node
 * 
 * @author alex
 *
 */
public class SelectorThread implements Runnable {

	private Selector selector;
	
	private Node searchNode;
	
	private SelectResult selectResult;
	
	private boolean moreResults = false;
	
	/**
	 * Построение объекта потока выборки узла
	 * @param source - корневой узел для поиска
	 * @param select - селектор, задающий критерии поиска
	 * @param func - функция-обработчик найденного результата
	 */
	public SelectorThread(Node source, Selector select, SelectResult func) {
		this(source, select, func, false);
	}
	
	/**
	 * Построение объекта потока выборки узла
	 * @param source - корневой узел для поиска
	 * @param select - селектор, задающий критерии поиска
	 * @param func - функция-обработчик найденного результата
	 * @param resultAsSet - допустим ли поиск множества
	 */
	public SelectorThread(Node source, Selector select, SelectResult func, boolean resultAsSet) {
		selector = select;
		searchNode = source;
		selectResult = func;
		moreResults = resultAsSet;
	}

	@Override
	public void run() {
		
		Node node = checkNode(searchNode);
		
		// если текущий узел является искомым
		if(node != null) {
			selectResult.result(node);
		}
		
		// ссылки на все дочерние элементы от текущего 
		// уровня сканирования в глубину
		ArrayList<Node> nextLevel = new ArrayList<>();
		
		// текущий уровень дочерних элементов поддерева
		ArrayList<Node> currentLevel = searchNode.getChildNodes();
		
		// пока есть, что перебирать
		while(currentLevel != null && currentLevel.size() > 0) {
			
			for(Node child : currentLevel) {
				
				// если найден искомый узел
				if(checkNode(child) != null) {
					selectResult.result(child);
					if(!moreResults) {
						return;
					}
				}
				
				// сохранение ссылок на следущий уровень
				nextLevel.addAll(child.getChildNodes());
			}
			
			// переключаемся на перебор следующего уровня
			currentLevel = nextLevel;
			
			// ссылки на "через уровень" сбрасываем
			nextLevel = new ArrayList<>();
		}
	}
	
	/**
	 * Проверка узла на соответствие условий селектора
	 * @param node- проверяемый узел
	 * @return null, если не соответствует селектору, иначе node.
	 */
	private Node checkNode(Node node) {
		return SemanticNetwork.checkNodeOnSelect(node, selector);
	}
}
