package ru.ifmo.cs.semnet.core.impl.utils;

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
	
	public static ExtSelectBuilder select() {
		return new ExtSelectBuilder();
	}
	
	/**
	 * 
	 * 
	 * @param type
	 * @param selector
	 * @return
	 */
	public ExtSelectBuilder ext(TypeLink type, Selector selector) {
		((ExtendedSelector)this.selector).addlinkageParameter(type, selector);
		return this;
	}
}
