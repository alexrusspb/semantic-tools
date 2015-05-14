package ru.ifmo.cs.semnet.core;

import java.util.HashMap;
import java.util.Locale;

/**
 * Класс, содержащий информацию, которой пользуемся 
 * при выборке узлов из сети. По этим параметрам и 
 * определям, подходит ли узел критериям поиска.
 * 
 * @author alex
 *
 */
public class Selector {

	private Locale locale;
	
	private HashMap<String, Object> selectParams;

	public Selector() {
		this(null);
	}
	
	public Selector(Locale loc) {
		if(loc == null) {
			locale = Options.getInstance().getDefaultLocale();
		} else {
			locale = loc;
		}
		selectParams = new HashMap<>();
	}
	
	public HashMap<String, Object> getSelectParams() {
		return selectParams;
	}

	public void setSelectParams(HashMap<String, Object> selectParams) {
		this.selectParams = selectParams;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public Object addSelectParam(String nameParam, Object valueParam) {
		return getSelectParams().put(nameParam, valueParam);
	}
}
