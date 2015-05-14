package ru.ifmo.cs.semnet.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

/**
 * Класс описывающий узел семантической сети.
 * 
 * @author Pismak Alexey
 */
public class Node implements Serializable {

	private static final long serialVersionUID = -7180176353700448140L;

	/* для общего параметра для всех локалей используй Locale.ROOT */
	private HashMap<Locale, HashMap<String, Object>> params;
	
	/* ссылки на другие узлы, связанные с текущим */
	private HashMap<Node, NodeLink> linksNodes;
	
	/* Helper'ы */
	public static final Locale RUSSIA = new Locale("ru");
	public static final String VIEW_OPTION = "view";
	public static final String PARENT_VIEW = "parent";
	
	/* используемое по умолчанию представление */
	private String bufferView = null;
	
	/* используемая по умолчанию локаль */
	private Locale bufferLocale = null;
	
	/**
	 * Инициализация узла с параметром представления узла
	 * для локали по умолчанию.
	 * 
	 * @param view - отображение понятия узла
	 * @see Options.getDefaultLocale
	 */
	public Node(String view) {
		this(view, null);
	}
	
	/**
	 * Инициализация узла с параметром представления
	 * узла для назначенной локали.
	 * 
	 * @param view - отображение понятия узла
	 * @param loc - для какой локали данное отображение
	 */
	public Node(String view, Locale loc) {
		params = new HashMap<>();
		linksNodes = new HashMap<>();
		
		Locale l = (loc == null) ? Options.getInstance().getDefaultLocale() : loc;
		
		bufferLocale = l;
		
		params.put(l, new HashMap<String, Object>());
		
		if(view != null) {
			bufferView = view;
			params.get(l).put(VIEW_OPTION, view);
		}
	}
	
	/**
	 * Чтение параметра по умолчанию
	 * 
	 * @return представление узла
	 */
	public String getDefaultView() {
		return bufferView;
	}
	
	/**
	 * Чтение параметра по умолчанию
	 * 
	 * @return локаль узла
	 */
	public Locale getDefaultLocale() {
		return bufferLocale;
	}
	
	/**
	 * Метод доступа к объету параметров
	 * 
	 * @return - карта параметров понятия
	 */
	protected HashMap<Locale, HashMap<String, Object>> getParams() {
		return params;
	}
	
	/**
	 * Метод доступа к объету параметров
	 * 
	 * @param newParams - новая карта параметров понятия
	 */
	protected void setParams(HashMap<Locale, HashMap<String, Object>> newParams) {
		if(newParams != null) {
			params = newParams;
		}
	}

	/**
	 * Метод доступа к ссылкам на другие узлы сети
	 * 
	 * @return карта ссылок на узлы сети
	 */
	public HashMap<Node, NodeLink> getLinks() {
		return linksNodes;
	}
	
	/**
	 * Метод доступа к ссылкам на другие узлы сети
	 * 
	 * @param новая карта ссылок на узлы сети
	 */
	protected void setLinks(HashMap<Node, NodeLink> newLinks) {
		if(newLinks != null) {
			linksNodes = newLinks;
		}
	}
	
	/**
	 * Метод для извлечения всех параметров узла по имен 
	 * параметра. При этом не играет роли к какой локали
	 * относится параметр с заданным именем. 
	 * 
	 * @param name - имя параметра
	 * @return - набор найденных параметров. Может быть пустым.
	 */
	public ArrayList<Object> selectParams(String name) {
		ArrayList<Object> result = new ArrayList<>();
		
		getParams().forEach((k, v) -> {
			Object paramValue = v.get(name);
			if(paramValue != null) {
				result.add(paramValue);
			}
		});
		
		return result;
	}
	
	/**
	 * Извлечение параметра узла сети по имени параметра.
	 * При этом используется локаль по умолчанию.
	 * 
	 * @param name - имя искомого параметра
	 * @return значение искомого параметра
	 * @throws LangNotSupportedException
	 * @see Options.getDefaultLocale
	 */
	public Object selectParam(String name) throws LangNotSupportedException {
		return selectParam(Options.getInstance().getDefaultLocale(), name);
	}
	
	/**
	 * Извлечение параметра понятия с заданным именем для 
	 * конкретной локали. 
	 * 
	 * @param locale - в какой локали искать параметр
	 * @param name - имя искомого понятия
	 * @return значение параметра понятия
	 * @throws LangNotSupportedException
	 */
	public Object selectParam(Locale locale, String name) throws LangNotSupportedException {
		if(locale == null) {
			locale = getDefaultLocale();
		}
		HashMap<String, Object> paramsForLang = getParams().get(locale);
		if(paramsForLang != null) {
			return paramsForLang.get(name);
		}
		throw new LangNotSupportedException(locale, this);
	}
	
	/**
	 * Костыль - выборка характеристики сети без try-catch
	 * 
	 * @param l - неоходимая локаль
	 * @param name - название характеристики узла
	 * @return - значение характеристики
	 */
	public Object selectParamInSafemode(Locale l, String name) {
		try {
			return selectParam(l, name);
		} catch (LangNotSupportedException ex) {
			return null;
		}
	}
	
	/**
	 * Внедрение нового параметра в понятие.
	 * Если такой параметр уже есть, то он не
	 * меняется. Используется локаль по умолчанию.
	 * 
	 * @param name - имя нового параметра
	 * @param value - значение нового параметра
	 * @return <b>true</b> если новое значение 
	 * 			внедрено, иначе <b>false</b>
	 */
	public boolean insertParam(String name, Object value) {
		return insertParam(Options.getInstance().getDefaultLocale(), name, value);
	}
	
	/**
	 * Внедрение нового параметра в понятие. Если
	 * такой параметр уже есть, то он не меняется.
	 * 
	 * @param locale - для какой локали параметр
	 * @param name - имя нового параметра
	 * @param value - значение нового параметра
	 * @return <b>true</b> если новое значение 
	 * 			внедрено, иначе <b>false</b>
	 */
	public boolean insertParam(Locale locale, String name, Object value) {
		HashMap<String, Object> paramsForLang = getParams().get(locale);
		if(paramsForLang == null) {
			paramsForLang = new HashMap<String, Object>();
			getParams().put(locale, paramsForLang);
		}
		Object currentValue = paramsForLang.get(name);
		if(currentValue != null) {
			return false;
		}
		paramsForLang.put(name, value);
		return true;
	}
	
	/**
	 * Модификация существующего параметра узла.
	 * Используется локаль по умолчанию.
	 * 
	 * @param name - имя параметра
	 * @param value - значение параметра
	 */
	public void modifyParam(String name, Object value) throws LangNotSupportedException {
		modifyParam(Options.getInstance().getDefaultLocale(), name, value);
	}
	
	/**
	 * Модификация существующего параметра узла.
	 * Если параметра нет, то он не добавляется.
	 * 
	 * @param locale - в какой локали модифицируем параметр
	 * @param name - имя параметра
	 * @param value - новое значение
	 */
	public void modifyParam(Locale locale, String name, Object value) throws LangNotSupportedException {
		HashMap<String, Object> paramsForLang = getParams().get(locale);
		if(paramsForLang == null) {
			throw new LangNotSupportedException(locale, this);
		}
		Object currentValue = paramsForLang.get(name);
		if(currentValue != null) {
			paramsForLang.put(name, value);
		}
	}
	
	@Override
	public String toString() {
		return getDefaultView();
	}
	
	/**
	 * Подробный вывод о узле сети:
	 * 	все его характеристики и краткие ссылки на другие узлы
	 * 
	 * @return - строка подробного описания узла
	 */
	public String toVerboseString() {
		StringBuilder sb = new StringBuilder();
		
		for(Locale l : getParams().keySet()) {
			sb.append("PARAMETERS FOR LOCALE [ ");
			sb.append(l.toString());
			sb.append(" ]:");
			
			for(String param : getParams().get(l).keySet()) {
				sb.append("\n  [");
				sb.append(param);
				sb.append("] -> ");
				sb.append(selectParamInSafemode(l, param));
			}
			sb.append("\n");
		}
		
		sb.append("LINKAGE SET");
		for(Node n : getLinks().keySet()) {
			sb.append("\n  [связь: ");
			sb.append(getLinks().get(n));
			sb.append("]: -> [ вид: \"");
			sb.append(n.bufferView);
			sb.append("\"]");
		}
		
		return sb.toString();
	}
	
	/**
	 * Чтение всех ссылок сети, которые являются 
	 * дочерними по отношению к текущей. Дочерний
	 * узел содержит понятие, которое имеет более
	 * конкретный смысл в какой либо области.
	 * 
	 * @return - все дочерние узлы
	 */
	public ArrayList<Node> getChildNodes() {
		ArrayList<Node> result = new ArrayList<>();
		// перебираем все ноды в ссылках
		for(Node n : getLinks().keySet()) {
			// если тип очередной ссылки CHILD, добавляем к результатам
			if(getLinks().get(n).equals(NodeLink.CHILD)) {
				result.add(n);
			}
		}
		return result;
	}
	
	/**
	 * Смена родителя текущего узла (перемещение узла в другую часть сети)
	 * 
	 * @param newParent - если null, то просто удалается текущий родитель
	 */
	public void changeParent(Node newParent) {
		if(getParent() != null) {
			for(Node n : getLinks().keySet()) {
				if(getLinks().get(n).equals(NodeLink.PARENT)) {
					getLinks().remove(n);
					break;
				}
			}
		}
		if(newParent != null) {
			getLinks().put(newParent, NodeLink.PARENT);
		}
	}
	
	/**
	 * Если у узла нет родителя, то он будет связан с текущим, как дочерний
	 * 
	 * @param child - узел, претендующий стать дочерним
	 */
	public void addChild(Node child) {
		if(child != null) {
			getLinks().put(child, NodeLink.CHILD);
			
			child.changeParent(this);
		}
	}
	
	private Node removeNode;
	
	/**
	 * Удаление дочернего ссылки на дочерний узел по заданным параметрам
	 * 
	 * @param view - представление узла
	 * @param locale - в заданной локали
	 */
	public void removeChild(String view, Locale locale) {
		removeNode = null;
		getLinks().forEach((k, v) -> {
			if(v.equals(NodeLink.CHILD)) {
				Object paramValue = k.selectParamInSafemode(locale, VIEW_OPTION);
				if(paramValue != null && paramValue.equals(view)) {
					removeNode = k;
				}
			}
		});
		
		if(removeNode != null) {
			getLinks().remove(removeNode);
		}
	}
	
	private Node resParent;
	
	/**
	 * Поиск ссылки на родительский узел
	 * 
	 * @return - родительский узел
	 */
	public Node getParent() {
		resParent = null;
		getLinks().forEach((k, v) -> {
			if(v.equals(NodeLink.PARENT)) {
				resParent = k;
			}
		});
		return resParent;
	}
	
	/**
	 * Подсчет количества всех дочерних элементов "в глубину"
	 * 
	 * @return - число дочерних элементов
	 */
	public long getCountChild() {
		Collection<Node> childs = getChildNodes();
		if(childs.size() == 0) {
			return 0;
		} else {
			long count = 0;
			for(Node n : childs) {
				count++;
				count += n.getCountChild();
			}
			return count;
		}
	}
}
