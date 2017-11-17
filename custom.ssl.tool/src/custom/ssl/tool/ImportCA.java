package custom.ssl.tool;

/**
 * Not in use
 */

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class ImportCA {

	public static void main(String[] argv) throws Exception {

	String certfile = "CACertificates/?????";
	FileInputStream is = new FileInputStream("/????");
	
	KeyStore keystore = KeyStore.getInstance("JKS");
	keystore.load(is, "????".toCharArray());
	
	String alias = "youralias";
	char[] password = "?????".toCharArray();
	
	CertificateFactory cf = CertificateFactory.getInstance("X.509");
	InputStream certstream = fullStream (certfile);
	Certificate certs =  cf.generateCertificate(certstream);
	
	File keystoreFile = new File("yourKeyStorePass.keystore");
	FileInputStream in = new FileInputStream(keystoreFile);
	keystore.load(in, password);
	in.close();
	
	keystore.setCertificateEntry(alias, certs);
	
	FileOutputStream out = new FileOutputStream(keystoreFile);
	keystore.store(out, password);
	out.close();
	
	}

	@SuppressWarnings("resource")
	private static InputStream fullStream ( String fname ) throws IOException {
		FileInputStream fis = new FileInputStream(fname);
		DataInputStream dis = new DataInputStream(fis);
		byte[] bytes = new byte[dis.available()];
		dis.readFully(bytes);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		return bais;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public static void ImportCertificateMethod(String Keystore, String Password, String CertificateFileName) 
			throws 
			CertificateException, 
			KeyStoreException, 
			NoSuchAlgorithmException, 
			IOException {
		
		Scanner sc=new Scanner(System.in);
		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		FileInputStream in1 = new FileInputStream(Keystore);
		ks.load(in1, Password.toCharArray());
		
		boolean foundCertificate = false;
		Enumeration aliasEnumumeration;
		List<String> AliasArray = new ArrayList<String>();;
		int AliasArrayInt =0;
		int WrongResponse = 0;
		boolean AliasPresent = false;
		
		BigInteger InputCertSN = CertificateDetails.CertificateSerialNumber(CertificateFileName);
		
		try {
			
			aliasEnumumeration = ks.aliases();
			
			while (aliasEnumumeration.hasMoreElements()) {
				
				String cAlias = (String) aliasEnumumeration.nextElement();
				AliasArray.add(cAlias);
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
				while (WrongResponse == 0) {
					
					System.out.print("\tEnter alias for the certificate "+CertificateFileName+" : ");  
					String InputAliasGiven=sc.next();
					if (CheckAlias(AliasArray, InputAliasGiven)) {
						int y=0;
					} else {
						
						InputStream certIn = ClassLoader.class.getResourceAsStream(CertificateFileName);
						BufferedInputStream bis = new BufferedInputStream(certIn);
					    CertificateFactory cf = CertificateFactory.getInstance("X.509");
					    while (bis.available() > 0) {
					        Certificate cert = cf.generateCertificate(bis);
					        ks.setCertificateEntry(InputAliasGiven, cert);
					    }

					    certIn.close();

					    OutputStream out = new FileOutputStream(Keystore);
					    ks.store(out, Password.toCharArray());
					    out.close();

						WrongResponse=1;
						System.out.println("\tCertificate: "+CertificateFileName+" added");
						break;
					}
					
				}
			}
			
		} catch (KeyStoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println();
		
	}

	private static boolean CheckAlias(List<String> AliasArray, String cAlias) {
		
		int AliasSize = AliasArray.toArray().length;
        for (int i=0; i<AliasSize; i++) {
            if ((AliasArray.toArray().equals(cAlias))) {
            	System.out.println("\tAlias "+cAlias+" already present in the keystore.");
            	return true;
            }
        }
		return false;
		
	}

}