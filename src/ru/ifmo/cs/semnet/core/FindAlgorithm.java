package ru.ifmo.cs.semnet.core;

import java.util.List;

/**
 * Перечисление доступных алгоритмов неточного поиска
 * с реализацией компараторов этих алгоритмов
 * 
 * @author Pismak Alexey
 * @lastUpdate 18 мая 2015 г.
 */
public enum FindAlgorithm implements Comparator {
	
	/**
	 * Алгоритм N-gramm для неточного поиска,
	 * основанный на количественной оценке
	 * совпадений фрагментов слов
	 */
	NGRAMM;
	
	/**
	 * Метод, осуществляющий выбор текущего алгоритма
	 * и передачи ему управления для сравнения строк
	 * (не смотря на то, что реализован один алгоритм,
	 * теоретически их может быть сколько угодно)
	 * 
	 * @param checkedWord проверяемое слово
	 * @param pattern шалон (слово которое стоит в условиях поиска)
	 * @return <code>true</code> если строки 
	 * 			прошли проверку, иначе <code>false</code>
	 */
	public boolean compare(String checkedWord, String pattern) {
		if(this.equals(NGRAMM)) {
			return nGrammComparator(checkedWord, pattern);
		}
		return false;
	}
	
	/**
	 * Метод N-Gramm. Перебор фрагментов слова и подсчет
	 * процента совпадений фрагментов.
	 * 
	 * @param word проверяемое слово
	 * @param pattern шалон (слово которое стоит в условиях поиска)
	 * @return <code>true</code> если строки 
	 * 			прошли проверку, иначе <code>false</code>
	 */
	private boolean nGrammComparator(String word, String pattern) {
		// не работает со словами разной длины
		if(word.length() - pattern.length() != 0) {
			return false;
		}
		int ok = 0, all = 0;
		// расчет длины n-грамм
		int l = calcLengthNGramm(word.length());
		// сравнение N-грамм без учета регистра
		for(int i = 0; i < word.length() - l + 1; i++) {
			if( word.substring(i, i + l)
					.equalsIgnoreCase(pattern.substring(i, i + l))) {
				ok++;
			}
			all++;
		}
		// расчет процента совпадений
		if( ((float)ok / (float)all) < 0.49f ) {
			return false;
		}
		return true;
	}
	
	/* вычисление длин N-грамм  */
	private int calcLengthNGramm(int wl) {
		if(wl <= 6) {
			return 2;
		}
		if(wl <= 12) {
			return 3;
		}
		return 4;
	}

	@Override
	public boolean compare(List<Object> network, Object selectorKey) {
		try {
			for(Object o : network) {
				if(  compare(  ((String)o), ((String)selectorKey)  )  ) {
					return true;
				}
			}
			return false;
		} catch (ClassCastException cce) {
			return network.contains(selectorKey);
		}
	}
}
