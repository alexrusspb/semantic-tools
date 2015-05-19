package ru.ifmo.cs.semnet.core.exception;

import ru.ifmo.cs.semnet.core.Node;

/**
 * Исключение,  вызванное  обнаружением  мутации  в  логической 
 * структуре  данных. Это  связано  с тем,  что в  семантической 
 * сети разделены представления логической и физической структур.
 * В  результате  неожиданного  удаления  фрагментов  физической
 * структуры на логическом  уровне возникает  данное  исключение.
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class MutatedNodeException extends RuntimeException {

	private static final long serialVersionUID = -972955934333948058L;
	
	/* мутировавший узел, операция в котором вызвала исключение */
	private final Node mutableNode;
	
	/* Сообщение с подробностью о причине исключения */
	private final String couseMutableMessage;
	
	/**
	 * Инициализация объекта исключения
	 * 
	 * @param node в каком узле произошло исключение
	 * @param couseMutable сообщение о причине мутации
	 */
	public MutatedNodeException(Node node, String couseMutable) {
		mutableNode = node;
		couseMutableMessage = couseMutable;
	}
	
	@Override
	public String getMessage() {
		return new StringBuilder().append("Node [").append(mutableNode.getId())
				.append("] was mutated cause: ").append(couseMutableMessage).toString();
	}
}
