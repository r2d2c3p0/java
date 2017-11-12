package custom.ssl.tool;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Scanner;

import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.io.pem.PemObject;

public class ExtractCertificates {
	
	@SuppressWarnings("resource")
	public static void ExtractCertificatesMain(String Keystore, String Password) 
			throws KeyStoreException, NoSuchAlgorithmException,	CertificateException, IOException {
		
		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		FileInputStream in1 = new FileInputStream(Keystore);
		ks.load(in1, Password.toCharArray());
		@SuppressWarnings("rawtypes")
		Enumeration aliasEnumumeration;
		Scanner sc=new Scanner(System.in);
		try {
			aliasEnumumeration = ks.aliases();int aNumber =0;			
			while (aliasEnumumeration.hasMoreElements()) {
				System.out.println();
				String cAlias = (String) aliasEnumumeration.nextElement();
				if (ks.isKeyEntry(cAlias)) {					
					System.out.println("\t|*|Alias (PrivateKey): "+cAlias);					
				} else {
					aNumber++;
					System.out.println("\t|"+aNumber+"|Alias: "+cAlias);
				}
				X509Certificate cert1 = (X509Certificate) ks.getCertificate(cAlias);
				System.out.println("\t\tSubject name: "+cert1.getSubjectDN());
				System.out.print("\tDownload above certificate from the keystore '"+Keystore+"'?: [y/n] ");
				String InputGiven=sc.next();
				if (InputGiven.equals("y")) {
					Certificate cert = ks.getCertificate(cAlias);
					StringWriter writer = new StringWriter();
					PEMWriter pemWriter = new PEMWriter(writer);
					pemWriter.writeObject(new PemObject("CERTIFICATE", cert.getEncoded()));
					pemWriter.flush();
					pemWriter.close();
					System.out.println(writer.toString());
					System.out.println("\tCertificate ["+cAlias+"] downloaded.");					
				} else {
					System.out.println("\tCertificate ["+cAlias+"] will be skipped.");
				}
			}
			System.out.println();
		} catch (KeyStoreException e1) {
			//e1.printStackTrace();
			System.out.println("\tERROR| ExtractCertificates.java KeystoreException occured.\n");
		}
	}

}