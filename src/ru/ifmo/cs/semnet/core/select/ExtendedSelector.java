package ru.ifmo.cs.semnet.core.select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ru.ifmo.cs.semnet.core.Node;
import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.TypeLink;

/**
 * Расширенный объект выборки.  Содержит  критерии поиска  
 * обычного   селектора:   анализа  и   проверки   ссылок,
 * парметров  и  допустимого  набора  идентификаторов.  К
 * функциям  расширения   относятся  проверки  свзязанных 
 * узлов отдельными селекторами. Т.е., например, необходимо
 * выбрать  все узлы, у  которых есть  дочерние  элементы с
 * параметром "area" -> "кулинария" и синонимом "Горячее блюдо".
 * А у искомых узлов должен быть параметр "area" -> "Кухня".
 * 
 * Простым объектом выборки это сделать не удастся. Синтаксис
 * запроса расширенного объекта для такого запроса примет вид:
 * 
 * SELECT WITH ( PARAM ("area" -> "Кухня") ) 
 * 			EXT (CHILD, WITH ( PARAM ( "area" -> "кулинария" ) )
 * 			EXT (SYNONYM, WIDTH ( PARAM ( "display" -> "Горячее блюдо" ) ) );
 * 
 * Как видно из примера, расширение селектора добавляет новую
 * функцию EXT, которая принимает тип ссылки, которая должна 
 * быть у искомых узлов. Эта функция позволяет "вкладывать"
 * один селектор в другой, что снимает ограничение на поиск в 
 * любую глубину и направление (по типам ссылок).
 * 
 * Любой из параметров можно опустить. Это дает возможность
 * гибко формировать запросы на выборку из сети. Примеры:
 * 
 * 	Выбрать узлы у которых родительские узлы будут будут иметь 
 * ссылки на синонимы понятию "Частица" на русском языке:
 * 
 * SELECT EXT 
 * 	(
 * 		PARENT, EXT 
 * 			(
 * 				SYNONYM, WITH
 * 					( 
 * 						PARAM ("display" -> "Частица") BY RU 
 * 					)
 * 			)
 * 	);	
 * 
 * Т.е. после SELECT следует череда функций, формирующих
 * объекты критериев выборки, которые затем будут переданы
 * сети для выполнения операций выборки. 
 * 
 * Программная реализация синтаксиса {@link=ExtSelectBuilder}
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class ExtendedSelector extends SimpleSelector {
	
	private Map<TypeLink, Selector> externalProtects;
	
	@Override
	public <T extends Node> boolean checkNode(T node) {
		return super.checkNode(node) && extendedCheck(node);
	}
	
	public void addlinkageParameter(TypeLink type, Selector selector) {
		if(selector != null) {
			if(externalProtects == null) {
				externalProtects = new HashMap<>();
			}
			if(!externalProtects.containsKey(type)) {
				externalProtects.put(type, selector);
			}
		}
	}
	
	protected boolean extendedCheck(Node node) {
		if(externalProtects == null) {
			return true;
		}
		for(Entry<TypeLink, Selector> e : externalProtects.entrySet()) {
			List<Node> links = node.getLinks(e.getKey());
			if(links == null || links.isEmpty()) {
				return false;
			}
			boolean subResult = false;
			for(Node lNode : links) {
				if(e.getValue().checkNode(lNode)) {
					subResult = true;
					break;
				}
			}
			if(!subResult) {
				return false;
			}
		}
		return true;
	}
	
}
