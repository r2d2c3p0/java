package org.r2d2c3p0.simpletext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	private static List<String> keystoreFinalList = new ArrayList<String>();
	
	public static void main(String[] args) {
		
		String hostName = GetHostName.run();
		
		try {
			String rootFile = args[0];
			File rFile = new File(rootFile);		
			keystoreFinalList = FindFiles
					.run(rFile, "jks");
			keystoreFinalList = FindFiles
					.run(rFile, "p12");
			keystoreFinalList = FindFiles
					.run(rFile, "JKS");
			keystoreFinalList = FindFiles
					.run(rFile, "P12");
			keystoreFinalList = FindFiles
					.run(rFile, "pfx");
			keystoreFinalList = FindFiles
					.run(rFile, "PFX");
			keystoreFinalList = FindFiles
					.run(rFile, "Jks");
			ListCertificates.run(keystoreFinalList, 
					"encrypted.file", hostName);
		} catch (ArrayIndexOutOfBoundsException aioobe) {
			System.err.println("ERROR| enter directory path to begin search.");
			System.exit(1);
		}
		
	}

}