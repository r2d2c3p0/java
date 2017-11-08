package custom.ssl.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;

public class ListCertificates {
	
	public static void ListCertificatesMain(String Keystore, String Password) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		
		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		FileInputStream in1 = new FileInputStream(Keystore);
		ks.load(in1, Password.toCharArray());
		@SuppressWarnings("rawtypes")
		Enumeration aliasEnumumeration;

		try {
			aliasEnumumeration = ks.aliases();int aNumber =0;			
			while (aliasEnumumeration.hasMoreElements()) {				
				String cAlias = (String) aliasEnumumeration.nextElement();
				if (ks.isKeyEntry(cAlias)) {
					System.out.println("\t\t|*|Alias (PrivateKey): "+cAlias);					
				} else {
					aNumber++;
					System.out.println("\t\t|"+aNumber+"|Alias: "+cAlias);
				}
				X509Certificate cert = (X509Certificate) ks.getCertificate(cAlias);
				System.out.println("\t\t\tSubject name: "+cert.getSubjectDN());
				System.out.println("\t\t\t\tIssued by: "+cert.getIssuerDN());
				System.out.println("\t\t\t\t\tSerial number: "+cert.getSerialNumber().toString());
				System.out.println("\t\t\t\t\t\tExpires on: "+cert.getNotAfter());      
				try {
					for (List<?> SAN: cert.getSubjectAlternativeNames()) {
						System.out.println("\t\t\t\t\t\t\tSAN entry: "+SAN.get(1));
					}
				} catch (Exception e1) {
					System.out.println("\t\t\t\t\t\t\tSAN entries: none");
				}				
				System.out.println();
			}
		} catch (KeyStoreException e1) {
			//e1.printStackTrace();
			System.out.println("\tERROR| ListCertificates.java KeystoreException occured.");
		}
	}

}