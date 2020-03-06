package custom.ssl.util;

import java.security.Provider;
import java.security.Security;
import java.util.Enumeration;

public class GetSecurityProviders {

	public static void GetProviders1() throws Exception {

	      Provider p[] = Security.getProviders();
	      for (int i = 0; i < p.length; i++) {
	        System.out.println("\t"+p[i]);
	        for (Enumeration<?> e = p[i].keys(); e.hasMoreElements();)
	          System.out.println("\t\t" + e.nextElement());
	      }
	      System.out.println();
	  }

	public static void GetProviders2() {

	    System.out.println("Providers installed on your system:");
	    Provider[] providerList = Security.getProviders();
	    for (int i = 0; i < providerList.length; i++) {
	      System.out.println((i + 1) + "] - Provider name: " + providerList[i].getName());
	      System.out.println("Provider version number: " + providerList[i].getVersion());
	      System.out.println("Provider information:\n" + providerList[i].getInfo());
	    }
	    System.out.println("\n");
	  }

}