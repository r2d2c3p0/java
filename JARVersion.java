package org.r2d2c3p0.jarversion;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javaxt.io.Jar;

public class Main {

	public static void main(String args[]) {
		
		Map<String, String> jarsWithVersionFound   = new LinkedHashMap<String, String>();
		List<String> jarsWithNoManifest     = new LinkedList<String>();
		List<String> jarsWithNoVersionFound = new LinkedList<String>();

		File[] files = new File(args[0]).listFiles();

		for(File file : files)
		{
		    String fileName = file.getName();


		    try
		    {
		        String jarVersion = new Jar(file).getVersion();

		        if(jarVersion == null)
		            jarsWithNoVersionFound.add(fileName);
		        else
		            jarsWithVersionFound.put(fileName, jarVersion);

		    }
		    catch(Exception ex)
		    {
		        jarsWithNoManifest.add(fileName);
		    }
		}

		System.out.println("******* JARs with versions found *******");
		for(Entry<String, String> jarName : jarsWithVersionFound.entrySet())
		    System.out.println(jarName.getKey() + " : " + jarName.getValue());

		System.out.println("\n \n ******* JARs with no versions found *******");
		for(String jarName : jarsWithNoVersionFound)
		    System.out.println(jarName);

		System.out.println("\n \n ******* JARs with no manifest found *******");
		for(String jarName : jarsWithNoManifest)
		    System.out.println(jarName);
		System.out.println("");
		
	}
}
