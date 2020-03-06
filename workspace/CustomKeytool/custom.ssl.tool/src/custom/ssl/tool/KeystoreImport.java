package custom.ssl.tool;

import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import custom.ssl.util.ChecksAndValidations;

/**
 * 	You can generate a pkcs12 file from PEM encoded certificate and key files using \
 * 	the following openssl command:
 *
 * 		openssl pkcs12 -export -out keystore.pkcs12 -in public.crt -inkey private.key
 *
*/

public class KeystoreImport {

   public static void KeystoreImportMain(String iKeystore, String oKeystore,
		   String inPassword, String outPassword) throws Exception {

	  boolean addedAlias = false;

	  if (iKeystore.equals(oKeystore)) {
		  System.out.println("\tError: Cannot import " + iKeystore+" in to "+oKeystore+".");
		  System.exit(1);
	  }

      File fileIn = new File(iKeystore); File fileOut = new File(oKeystore);
      KeyStore iks = ChecksAndValidations.PreChecksAndValidations(iKeystore);
      KeyStore oks = ChecksAndValidations.PreChecksAndValidations(oKeystore);

      iks.load(new FileInputStream(fileIn), inPassword.toCharArray());
      oks.load((fileOut.exists()) ? new FileInputStream(fileOut) : null, outPassword.toCharArray());

      @SuppressWarnings("rawtypes")
      Enumeration eAliases = iks.aliases();
      int n = 0;
      System.out.println();

      while (eAliases.hasMoreElements()) {

    	  String strAlias = (String)eAliases.nextElement();
    	  System.out.println("\t\tAlias " + n++ + ": " + strAlias);

    	  if (oks.containsAlias(strAlias)) {
    		  System.out.println("\t\tAlias "+strAlias+" already present in "+oKeystore);
    	  } else {
    		  if (iks.isKeyEntry(strAlias)) {
        		 System.out.println("\t\tAdding key for alias " + strAlias);
        		 Key key = iks.getKey(strAlias, inPassword.toCharArray());
        		 Certificate[] chain = iks.getCertificateChain(strAlias);
        		 oks.setKeyEntry(strAlias, key, outPassword.toCharArray(), chain);
        		 addedAlias = true;
        	 } else {
        		 System.out.println("\t\tAlias " + strAlias+" is a signer, will be skipped...");
        	 }
    	  }
      }

      if (addedAlias) {
    	  OutputStream out = new FileOutputStream(fileOut);
    	  oks.store(out, outPassword.toCharArray());
    	  out.close();
    	  System.out.println("\t\tImport " + iKeystore+" in to "+oKeystore+" completed.");
      }
      System.out.println();
   }

   static void dumpChain(Certificate[] chain) {

      for (int i = 0; i < chain.length; i++) {
         Certificate cert = chain[i];
         if (cert instanceof X509Certificate) {
            X509Certificate x509 = (X509Certificate)chain[i];
            System.out.println("\tSubject: " + x509.getSubjectDN());
            System.out.println("\tIssuer: " + x509.getIssuerDN());
         }

      }
   }

}