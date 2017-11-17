package custom.ssl.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Scanner;
import java.math.BigInteger;

public class ImportCertificates {
	
	@SuppressWarnings({ "rawtypes", "unused", "resource" })
	public static void ImportCertificatesMethod(String Keystore, String Password, String CertificateFileName) throws
			CertificateException, 
			KeyStoreException, 
			NoSuchAlgorithmException, 
			IOException {
		
		boolean foundCertificate = false;
		Enumeration aliasEnumumeration;
		int WrongResponse = 0;
		boolean AliasPresent = false;
		Scanner sc=new Scanner(System.in);
		
		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		FileInputStream in1 = new FileInputStream(Keystore);
		ks.load(in1, Password.toCharArray());
		BigInteger InputCertSN = CertificateDetails.CertificateSerialNumber(CertificateFileName);
		
		try {			
			aliasEnumumeration = ks.aliases();			
			while (aliasEnumumeration.hasMoreElements()) {				
				String cAlias = (String) aliasEnumumeration.nextElement();
				X509Certificate cert = (X509Certificate) ks.getCertificate(cAlias);
				BigInteger CurrentCertSN = cert.getSerialNumber();				
				if (CurrentCertSN.compareTo(InputCertSN) == 0) {
					foundCertificate=true;
				}
			}			
			if (foundCertificate) {				
				System.out.println("\t"+CertificateFileName+" already present in the keystore.");				
			} else {				
				System.out.println("\t"+CertificateFileName+" NOT present in the keystore.");
				System.out.print("\tAdd above certificate to the keystore '"+Keystore+"'?: [y/n] ");
				String InputGiven=sc.next();
				if (InputGiven.equals("y")) {
					while (WrongResponse == 0) {					
						System.out.print("\tEnter alias for the certificate "+CertificateFileName+" : ");  
						String InputAliasGiven=sc.next();				    
				    	if (ks.containsAlias(InputAliasGiven)) {
				        	System.out.println("\tAlias "+InputAliasGiven+" already present in the keystore.");
				        	//return;
				    	} else {
				    		FileInputStream is = new FileInputStream(Keystore);
				    		ks.load(is, Password.toCharArray());
				    		char[] password = Password.toCharArray();
				    		CertificateFactory cf = CertificateFactory.getInstance("X.509");
				    		InputStream certstream = ChecksAndValidations.fullStream(CertificateFileName);
				    		Certificate certs =  cf.generateCertificate(certstream);
				    		File keystoreFile = new File(Keystore);
				    		FileInputStream in = new FileInputStream(keystoreFile);
				    		ks.load(in, password);
				    		in.close();
				    		ks.setCertificateEntry(InputAliasGiven, certs);
				    		FileOutputStream out = new FileOutputStream(keystoreFile);
				    		ks.store(out, password);
				    		out.close();
				    		WrongResponse=1;				    	
				    		System.out.println("\t"+CertificateFileName+"[Alias "+InputAliasGiven+"] added to the keystore.");
				    	}
					}
				} else {
					System.out.println("\t"+CertificateFileName+" skipped.");
				}
			}			
		} catch (KeyStoreException e1) {
			//e1.printStackTrace();
			System.out.println("\tERROR| ImportCertificates.java KeystoreException occured.\n");
		}
		System.out.println();
	}
	
}