package ru.ifmo.cs.semnet.core;

import java.util.Map;

@FunctionalInterface
public interface LinkResolver {
	
	/**
	 * Метод, разрешающий конфликты ссылок пр модификации сети.
	 * К такого рода операциям относится удаление узлов и вставка
	 * узлов. 
	 * 
	 * @param modifySource
	 * @param storage
	 * @param semNet
	 * @return 
	 */
	<T extends Node> T resolve(Node modifySource, Map<Long, T> storage, SemanticNetwork<T> semNet);
	
}
