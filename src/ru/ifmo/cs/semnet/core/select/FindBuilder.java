package ru.ifmo.cs.semnet.core.select;

import java.util.Locale;

import ru.ifmo.cs.semnet.core.Finder;

public class FindBuilder {

	private SimpleFinder finder;
	
	private FindBuilder() {
		finder = new SimpleFinder();
	}
	
	public static FindBuilder find(String where, String what) {
		return new FindBuilder().and(where, what);
	}
	
	public FindBuilder and(String where, String what) {
		finder.addParam(where, what);
		return this;
	}
	
	public Finder build(Locale byLang) {
		return finder;
	}
}
