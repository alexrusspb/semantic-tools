package ru.ifmo.cs.semnet.core;

/**
 * Класс, экземпляр которо дает понять, как сети
 * реагировать на удачение и вставки узлов. Как
 * склеивать "дыры" в сети после удаления, как 
 * разрешать конфликты с сылками при операциях,
 * модифицирующих сеть.
 * 
 * FIXME расширить параметры
 * 
 * @author alex
 *
 */
public class ModifyOptions {

	/* присоединять ли дочерние узлы к родительскому */
	private boolean concatenateChildrenToParent;

	/* присоединять ли дочерние узлы к корневому */
	private boolean concatenateChildrenToRoot;
	
	/* заменить ли узел, как корневой */
	private boolean replaceWithRoot;
	
	private Node keyNode;
	
	/* как дочерний узел */
	private boolean childForNode;
	 
	/* перемещать ли дочерние узлы */
	private boolean moveChild;
	
	public boolean isConcatenateChildrenToParent() {
		return concatenateChildrenToParent;
	}

	public void setConcatenateChildrenToParent(boolean concatenateChildrenToParent) {
		this.concatenateChildrenToParent = concatenateChildrenToParent;
	}

	public boolean isConcatenateChildrenToRoot() {
		return concatenateChildrenToRoot;
	}

	public void setConcatenateChildrenToRoot(boolean concatenateChildrenToRoot) {
		this.concatenateChildrenToRoot = concatenateChildrenToRoot;
	}

	public boolean isReplaceWithRoot() {
		return replaceWithRoot;
	}

	public void setReplaceWithRoot(boolean replaceWithRoot) {
		this.replaceWithRoot = replaceWithRoot;
	}

	public boolean isChildForNode() {
		return childForNode;
	}

	public void setChildForNode(boolean childForNode) {
		this.childForNode = childForNode;
	}

	public boolean isMoveChild() {
		return moveChild;
	}

	public void setMoveChild(boolean moveChild) {
		this.moveChild = moveChild;
	}
	
	public Node getKeyNode() {
		return keyNode;
	}
	
	public void setKeyNode(Node node) {
		this.keyNode = node;
	}
	
	/**
	 * Создает экземпляр простого удаления
	 * @return
	 */
	public static ModifyOptions CreateEasyRemoveOptions() {
		ModifyOptions options = new ModifyOptions();
		options.setConcatenateChildrenToParent(true);
		return options;
	}
	
	/**
	 * Создает инстанс для простого инсерта c перемещениями дочерних узлов
	 * 
	 * @param parentNode
	 * @return
	 */
	public static ModifyOptions CreatEasyInsertOptions(Node parentNode) {
		ModifyOptions mo = new ModifyOptions();
		
		mo.keyNode = parentNode;
		mo.setMoveChild(true);
		mo.setChildForNode(true);
		
		return mo;
	}
}
