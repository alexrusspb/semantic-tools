package ru.ifmo.cs.semnet.core.select;

import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.TypeLink;

/**
 * Класс-строить объектов-селекторов. Реализует 
 * программный аналог синтаксиса, приведенного в
 * описании {@link=ExtendedSelector}. 
 * 
 * SELECT EXT 
 * 	(
 * 	  PARENT, EXT 
 * 	    (
 * 		  SYNONYM, WITH
 * 			( 
 * 			  PARAM ("display" -> "Частица") BY RU 
 * 			)
 * 		)
 * 	);
 * 
 * Эквивалентно
 * 
 * ExtSelectBuilder.select().ext
 *   (
 *     TypeLink.PARENT,  ExtSelectBuilder.select().ext
 *       (
 *         TypeLink.SYNONYM, SelectBuilder.select().param("display", "Частица").by(RUSSIA)
 * 		 )
 * 	 );
 * 
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class ExtSelectBuilder extends SelectBuilder {

	public ExtSelectBuilder() {
		selector = new ExtendedSelector();
	}
	
	/**
	 * @return новый экземпляр ExtSelectBuilder
	 */
	public static ExtSelectBuilder select() {
		return new ExtSelectBuilder();
	}
	
	/**
	 * Добавление расширенного критерия для внешних узлов
	 * 
	 * @param type тип связи узлов, на которые будут накладываться 
	 * 					расширенные критерии
	 * @param selector объект критериев выбоки
	 * @return текущий экземпляр {@link ExtSelectBuilder}
	 */
	public ExtSelectBuilder ext(TypeLink type, Selector selector) {
		((ExtendedSelector)this.selector).addlinkageParameter(type, selector);
		return this;
	}
}
