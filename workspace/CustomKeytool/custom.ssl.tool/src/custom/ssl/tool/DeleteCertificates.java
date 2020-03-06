/***
 * Added code to check to see if the certificate has expired and let the user delete that certificate from the
 * key store.
 * updated 1/19/2018
 * 
 */
package custom.ssl.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Scanner;

import custom.ssl.util.ChecksAndValidations;

public class DeleteCertificates {

	private static final long MM_SECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	private static final Date currentDate = new Date();
	private static final long currentTime = currentDate.getTime();
	
	@SuppressWarnings("resource")
	public static void DeleteCertificatesMain(String Keystore, String Password) throws
		KeyStoreException,
		NoSuchAlgorithmException,
		CertificateException,
		IOException {

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
				X509Certificate cert = (X509Certificate) ks.getCertificate(cAlias);
				System.out.println("\t\tSubject name: "+cert.getSubjectDN());
				
				
				/***
				 * Added 1/19/2018 - Easy access to delete the certificates that are expired.
				 */
				Date expiredCertificate = cert.getNotAfter();
				long expTime = expiredCertificate.getTime();
				int dayOffSet = (int) ((expTime - currentTime) / MM_SECONDS_PER_DAY);
				if (dayOffSet <= 0) {
					System.out.println("\n\t*** Warning *** certificate has expired ( "+ cert.getNotAfter() +" )\n");
				}
				
				
				System.out.print("\tDelete above alias/certificate from the keystore '"+Keystore+"'?: [y/n] ");
				String InputGiven=sc.next();
				if (InputGiven.equals("y")) {
					FileInputStream is = new FileInputStream(Keystore);
					ks.load(is, Password.toCharArray());
					char[] password = Password.toCharArray();
					File keystoreFile = new File(Keystore);
					FileInputStream in = new FileInputStream(keystoreFile);
					ks.load(in, password);
					in.close();
					ks.deleteEntry(cAlias);
					FileOutputStream out = new FileOutputStream(keystoreFile);
					ks.store(out, password);
					out.close();
					System.out.println("\tAlias ["+cAlias+"] deleted from the keystore.");
				} else {
					System.out.println("\tAlias ["+cAlias+"] will be skipped.");
				}
			}
			System.out.println();
		} catch (KeyStoreException e1) {
			System.err.println("\tERROR| DeleteCertificates.java KeystoreException occured.\n");
		}
	}

}