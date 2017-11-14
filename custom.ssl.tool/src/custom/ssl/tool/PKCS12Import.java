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

/**
 * You can generate a pkcs12 file from PEM encoded certificate and key files
 * using the following openssl command:
 * 	<PRE>
 *    openssl pkcs12 -export -out keystore.pkcs12 -in public.crt -inkey private.key
 * 	</PRE>
*/

public class PKCS12Import {
	
   public static void PKCS12ImportMain(String iKeystore, String oKeystore, String inPassword, String outPassword) 
		   throws Exception {
	   
      File fileIn = new File(iKeystore); File fileOut = new File(oKeystore);      
      KeyStore iks = ChecksAndValidations.PreChecksAndValidations(iKeystore);
      KeyStore oks = ChecksAndValidations.PreChecksAndValidations(oKeystore);
      iks.load(new FileInputStream(fileIn), inPassword.toCharArray());

      oks.load((fileOut.exists()) ? new FileInputStream(fileOut) : null, outPassword.toCharArray());

      @SuppressWarnings("rawtypes")
      Enumeration eAliases = iks.aliases();int n = 0;
      while (eAliases.hasMoreElements()) {
    	  
         String strAlias = (String)eAliases.nextElement();
         System.out.println("\tAlias " + n++ + ": " + strAlias);
         
         if (iks.isKeyEntry(strAlias)) {
            System.out.println("\tAdding key for alias " + strAlias);
            Key key = iks.getKey(strAlias, inPassword.toCharArray());

            Certificate[] chain = iks.getCertificateChain(strAlias);

            oks.setKeyEntry(strAlias, key, outPassword.toCharArray(), chain);
         }

      }

      OutputStream out = new FileOutputStream(fileOut);
      oks.store(out, outPassword.toCharArray());
      out.close();
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