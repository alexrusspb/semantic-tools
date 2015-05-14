package ru.ifmo.cs.semnet.importer.test;

import java.util.Locale;

import org.junit.Test;

import ru.ifmo.cs.semnet.core.Options;
import ru.ifmo.cs.semnet.importer.ImportMode;

public class OptionsSaveAndRestore {

	
	
	@Test
	public void testSave() {
		
		System.out.println(Options.getInstance().toString());
		
		Options.getInstance().changeCronTimeForUpdateImport(10);
		Options.getInstance().changeDefaultLocale(Locale.ENGLISH);
		Options.getInstance().changeDefaultImportMode(ImportMode.DEFAULT);
		
		Options.save();
		
		System.out.println(Options.getInstance().toString());
		
	}

}
