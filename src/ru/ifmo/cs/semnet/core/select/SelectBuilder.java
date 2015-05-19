package ru.ifmo.cs.semnet.core.select;

import java.util.Locale;

import ru.ifmo.cs.semnet.core.Selector;
import ru.ifmo.cs.semnet.core.TypeLink;

/**
 * Класс-строить объектов-селекторов. Реализует 
 * программный аналог синтаксиса, приведенного в
 * описании {@link=SimpleSelector}. 
 * 
 * SELECT WITH ( 
 * 			ID (1, 2, 3), 
 * 			LINK (2 -> CHILD, 3 -> PARENT), 
 * 			PARAM ("display" -> "стол")
 * 				) BY RU;
 * 
 * Эквивалентно
 * 
 * SelectBuilder.select()
 *					.id(1, 2, 3)
 *					.link(2, TypeLink.CHILD).link(3, TypeLink.PARENT)
 *					.param("display", "стол")
 *					.by(SemNetUtils.RUSSIA)
 *					.build();
 * 
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public class SelectBuilder {
	
	protected SimpleSelector selector;
	
	protected SelectBuilder() {
		selector = new SimpleSelector();
		selector.setComparator(new SelectComparator());
	}
	
	/**
	 * @return предоставляет "строителя" для селектора
	 */
	public static SelectBuilder select() {
		return new SelectBuilder();
	}
	
	/**
	 * Добавляет перечень идентификаторов, которые должны иметь
	 * (один из указанных) проверяемые селектором узлы
	 * 
	 * @param ids допустимые идентификаторы
	 * @return {@link SelectBuilder}
	 */
	public SelectBuilder id(long ...ids) {
		if(ids != null && ids.length > 0) {
			for(long l : ids) {
				selector.addId(l);
			}
		}
		return this;
	}
	
	/**
	 * Добавление ограничения на наличие ссылки
	 * 
	 * @param id на что должен ссылаться узел
	 * @param type каков тип связи должен быть
	 * @return {@link SelectBuilder}
	 */
	public SelectBuilder link(long id, TypeLink type) {
		if(type != null) {
			selector.addLink(id, type);
		}
		return this;
	}
	
	/**
	 * Добавление ограничений по параметрам
	 * 
	 * @param name имя параметра
	 * @param value значение параметра
	 * @return {@link SelectBuilder}
	 */
	public SelectBuilder param(String name, Object value) {
		if(name != null && value != null) {
			selector.addParam(name, value);
		}
		return this;
	}
	
	/**
	 * Ограничение параметров по локали
	 * 
	 * @param locale в какой локали сканировать параметры
	 * @return {@link SelectBuilder}
	 */
	public SelectBuilder by(Locale locale) {
		if(locale != null) {
			selector.changeLocaleProtect(locale);
		}
		return this;
	}
	
	/**
	 * @return сформированный экземпляр {@link Selector}
	 */
	public Selector build() {
		return selector;
	}
}
