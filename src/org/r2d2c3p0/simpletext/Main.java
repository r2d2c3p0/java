package org.r2d2c3p0.simpletext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	private static List<String> keystoreFinalList = new ArrayList<String>();
	
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner InputOperation = new Scanner(System.in);
		String hostName;
		String appName;
		String rootFile;
		System.out.print("\tEnter the hostname: ");
		hostName = InputOperation.next();
		System.out.print("\tEnter the appname: ");
		appName = InputOperation.next();
		System.out.print("\tEnter the directory: ");
		rootFile = InputOperation.next();
		
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
		ListCertificates.run(keystoreFinalList, "encrypted.password", appName, hostName);
	}

}