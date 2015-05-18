package ru.ifmo.cs.semnet.core;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum FindAlgorithm {
	
	/**
	 * Алгоритм N-gramm для неточного поиска,
	 * основанный на количественной оценке
	 * совпадений фрагментов слов
	 */
	NGRAMM,
	
	/**
	 * Алгоритм расширенной выборки путем перебора
	 * и перестановки букв исходного слова буквами
	 * алфавита.
	 */
	EXPANSION_SELECT_PERMUTATION;
	
	private FindAlgorithm() {
		supportLanguages = new HashMap<>();
	}
	
	private Map<Locale, Character[]> supportLanguages;
	
	public void registerAlphabet(Locale locale, Character[] letters) {
		if(!supportLanguages.containsKey(locale)) {
			supportLanguages.put(locale, letters);
		}
	}
	
	public boolean compare(String checkedWord, String pattern, Locale locale) {
		if(supportLanguages.containsKey(locale)) {
			if(this.equals(NGRAMM)) {
				return nGrammComparator(checkedWord, pattern);
			}
			if(this.equals(EXPANSION_SELECT_PERMUTATION)) {
				return expansionSelect(checkedWord, pattern);
			}
		}
		return false;
	}
	
	private boolean nGrammComparator(String word, String pattern) {
		// TODO 
		return false;
	}
	
	private boolean expansionSelect(String word, String pattern) {
		// TODO
		return false;
	}
}
