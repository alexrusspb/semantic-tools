package ru.ifmo.cs.semnet.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Locale;

import ru.ifmo.cs.semnet.importer.ImportMode;

public class Options implements Serializable {

	private static final long serialVersionUID = 4742943375354831141L;
	
	private static final String pathToFile = "config/options.cfg";

	private Locale defaultLocale = Locale.ENGLISH;
	
	private ImportMode defaultImportMode = ImportMode.DEFAULT;
	
	private int cronTimeForUpdateImport = 10;
	
	private static Options instance;
	
	private Options() {
		defaultLocale = Locale.ENGLISH;
	}
	
	static {
		instance = new Options();
		instance.restore();
	}
	
	public static Options getInstance() {
		return instance;
	}
	
	public Locale getDefaultLocale() {
		return defaultLocale;
	}
	
	public void changeDefaultLocale(Locale locale) {
		defaultLocale = locale;
	}

	public ImportMode getDefaultImportMode() {
		return defaultImportMode;
	}

	public void changeDefaultImportMode(ImportMode defaultImportMode) {
		this.defaultImportMode = defaultImportMode;
	}

	public int getCronTimeForUpdateImport() {
		return cronTimeForUpdateImport;
	}

	public void changeCronTimeForUpdateImport(int cronTimeForUpdateImport) {
		this.cronTimeForUpdateImport = cronTimeForUpdateImport;
	}
	
	public static void save() {
		try {
			
			File file = getFile();
			
			OutputStream fs = new FileOutputStream(file);
			OutputStream buffer = new BufferedOutputStream(fs);
			ObjectOutput output = new ObjectOutputStream(buffer);
			
			output.writeObject(instance);
			
			output.flush();
			output.close();
		} catch(IOException exept) {
			System.out.println("печаль при сохранении параметров");
			exept.printStackTrace();
		}
 	}
	
	private void restore() {
		try {
			
			File file = getFile();
			
			InputStream fs = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(fs);
			ObjectInput in = new ObjectInputStream(buffer);
			
			instance = (Options)in.readObject();
			in.close();
			
		} catch(IOException | ClassNotFoundException exept) {
			System.out.println("печаль при восстановлении параметров");
			exept.printStackTrace();
		}
	}
	
	private static File getFile() {
		File file = null;
	    URL res = instance.getClass().getClassLoader().getResource(pathToFile);
	    if (res.toString().startsWith("jar:")) {
	        try {
	            InputStream input = instance.getClass().getResourceAsStream(pathToFile);
	            file = File.createTempFile("tempfile", ".tmp");
	            OutputStream out = new FileOutputStream(file);
	            int read;
	            byte[] bytes = new byte[1024];

	            while ((read = input.read(bytes)) != -1) {
	                out.write(bytes, 0, read);
	            }
	            file.deleteOnExit();
	            out.close();
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
	    } else {
	        /* если из IDE */
	        file = new File(res.getFile());
	    }

	    if (file != null && !file.exists()) {
	        throw new RuntimeException("Error: File " + file + " not found!");
	    }
	    
	    return file;
	}
	
	@Override
	public String toString() {
		return "[" + getDefaultLocale().toString() + "] [" 
				+ getDefaultImportMode().toString() + "] ["
				+ getCronTimeForUpdateImport() + "]"; 
	}
}
