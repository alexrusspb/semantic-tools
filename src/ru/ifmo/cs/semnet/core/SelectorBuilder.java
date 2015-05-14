package ru.ifmo.cs.semnet.core;

import java.util.Locale;

public class SelectorBuilder {
	
	private Selector selector;
	
	private SelectorBuilder(Locale loc) {
		selector = new Selector(loc);
	}
	
	public static SelectorBuilder Create() {
		return new SelectorBuilder(null);
	}
	
	public static SelectorBuilder Create(Locale locale) {
		return new SelectorBuilder(locale);
	}
	
	public SelectorBuilder appendParameter(String name, Object value) {
		if(!isNullOrEmpty(name) && value != null) {
			selector.addSelectParam(name, value);
		}
		return this;
	}
	
	public Selector build() {
		return selector;
	}

	private static boolean isNullOrEmpty(String name) {
		return name == null || name.isEmpty();
	}
}
