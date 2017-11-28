package com.sat.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ShowClassesInJarFile {
	
	public static void main(String[] args) throws IOException {
	
		List<String> classNames = new ArrayList<String>();
		ZipInputStream zip;
		
		try {
			
			zip = new ZipInputStream(new FileInputStream("C:\\Users\\REDDYFAMILY\\Desktop\\srtool.jar"));
			
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					String className = entry.getName().replace('/', '.');
					classNames.add(className.substring(0, className.length() - ".class".length()));
				}
				
			}
			
			for (int i=0;i<classNames.size();i++) {
				System.out.println(classNames.get(i));
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("Jar not found!");
			System.exit(1);
		}
		
	}
}