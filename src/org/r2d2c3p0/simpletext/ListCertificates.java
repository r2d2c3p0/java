package org.r2d2c3p0.simpletext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

public class ListCertificates {

	public static void run(List<String> fileFinalList, String encryptedFile, String hostName) {
		for (String keystore : fileFinalList) {
			String final_string=null;
			KeyStore keystoreInstance;
			try {
				keystoreInstance = ChecksAndValidations.PreChecksAndValidations(keystore);
				try {
					keystoreInstance.load(new FileInputStream(keystore),
							KeystorePassword.run(keystore, keystoreInstance, encryptedFile)
							.toCharArray());
					Enumeration<String> aliasEnumeration = keystoreInstance.aliases();
					while (aliasEnumeration.hasMoreElements()) {
						String cAlias = aliasEnumeration.nextElement();						
						X509Certificate x509certificate =
								(X509Certificate) keystoreInstance
								.getCertificate(cAlias);
						Date expTime = x509certificate.getNotAfter();
						long expTimeL = expTime.getTime();						
						final_string = x509certificate.getSubjectDN().toString()+ " |"
								+ " "+GetLocalTime.run(expTimeL)+ " | "+ hostName+" | "+keystore;
						System.out.println(final_string);
					}
				} catch (NoSuchAlgorithmException nsae) {
					System.out.println(" [SimpleText] "+nsae+" "+keystore);
				} catch (CertificateException ce) {
					System.out.println(" [SimpleText] "+ce+" "+keystore);
				} catch (FileNotFoundException fnfe) {
					System.out.println(" [SimpleText] "+fnfe+" "+keystore);
				} catch (IOException ioe) {
					System.out.println(" [SimpleText] "+ioe+" "+keystore);
				} catch (NullPointerException npe) {
					System.out.println(" [password help] "+keystore);
				}
			} catch (KeyStoreException kse) {
				System.out.println(" [SimpleText] KeyStoreException occured "+keystore);
			}
		}
	}
}