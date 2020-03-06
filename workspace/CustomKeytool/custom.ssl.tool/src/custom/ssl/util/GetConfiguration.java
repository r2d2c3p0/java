package custom.ssl.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class GetConfiguration {
	
	private static String PATH = "./customkeytool.properties";

	public static String getEncryptedFile() {

	    String encFile = null;
	    Properties mainProperties = new Properties();
	    FileInputStream file;
	    
	    try {
			file = new FileInputStream(PATH);
			try {
				mainProperties.load(file);
			} catch (IOException e) {
				System.out.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
				file.close();
			} catch (IOException e) {
				System.out.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
		    	encFile = mainProperties.getProperty("encrypted.file.path");
		    	return encFile;
		    } catch (Exception e) {
		    	System.out.println("\t Error: property 'encrypted.file.path' not set???");
				System.exit(1);
		    }
		} catch (FileNotFoundException fnfe) {
			System.out.println("\t Error: File "+PATH+" not found.");
			System.exit(1);		
		}
    
	    return null;

	}
	
	public static String getCADirectory() {

	    String calocation = null;
	    Properties mainProperties = new Properties();
	    FileInputStream file;
	    
	    try {
			file = new FileInputStream(PATH);
			try {
				mainProperties.load(file);
			} catch (IOException e) {
				System.out.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
				file.close();
			} catch (IOException e) {
				System.out.println("\t Error: File "+PATH+" io exception.");
				System.exit(1);
			}
		    try {
		    	calocation = mainProperties.getProperty("ca.location");
		    	return calocation;
		    } catch (Exception e) {
		    	System.out.println("\t Error: property 'ca.location' not set???");
				System.exit(1);
		    }
		} catch (FileNotFoundException fnfe) {
			System.out.println("\t Error: File "+PATH+" not found.");
			System.exit(1);
		}
	    return null;

	}
}