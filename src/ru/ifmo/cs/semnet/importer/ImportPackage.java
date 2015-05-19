package ru.ifmo.cs.semnet.importer;

import java.util.Locale;
import java.util.Map;

import ru.ifmo.cs.semnet.core.TypeLink;

/**
 * Описание пакета получаемых данных
 * 
 * @author Pismak Alexey
 * @lastUpdate 19 мая 2015 г.
 */
public interface ImportPackage {
	
	/**
	 * @return представление узла
	 */
	String getView();
	
	/**
	 * @param view представление узла
	 */
	void setView(String view);
	
	/**
	 * @return представление узла, который 
	 * 		нужно назначить родительским
	 */
	String getParentView();
	
	/**
	 * @param pView представление родительского узла
	 */
	void setParentView(String pView);
	
	/**
	 * @return параметры инициализации узла
	 */
	Map<String, Object> getInitParameters();
	
	/**
	 * Добавление параметра инициализации
	 * 
	 * @param name имя параметра
	 * @param value значение параметра
	 */
	void addInitParameter(String name, Object value);
	
	/**
	 * @return набор представлений, которые могут быть 
	 * 		связаны с новым узлом, если будут найдены в сети
	 */
	Map<String, TypeLink> getLinkCandidates();
	
	/**
	 * Добавление кандидата в набор связей нового узла
	 * 
	 * @param viewCandidate представление кандидита
	 * @param linkage тип необходимой связи
	 */
	void addLinkCandidate(String viewCandidate, TypeLink linkage);
	
	/**
	 * @return локаль для которой задано представление узла
	 */
	Locale getLocaleForView();
	
	/**
	 * @param locale завадаемая локаль
	 */
	void setLocaleForView(Locale locale);
}
