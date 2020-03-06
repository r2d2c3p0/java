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

import custom.ssl.util.ChecksAndValidations;

public class KeystoreCleaner {
	
	private static final long MM_SECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	private static final Date currentDate = new Date();
	private static final long currentTime = currentDate.getTime();
	
	public static void main(String[] args) throws
		KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		
		String Keystore = args[0];
		String Password = args[1];

		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		FileInputStream in1 = new FileInputStream(Keystore);
		ks.load(in1, Password.toCharArray());
		@SuppressWarnings("rawtypes")
		Enumeration aliasEnumumeration;
		
		try {
			aliasEnumumeration = ks.aliases();int aNumber =0;int totalCertificatesDeleted=0;
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
				Date expiredCertificate = cert.getNotAfter();
				long expTime = expiredCertificate.getTime();
				int dayOffSet = (int) ((expTime - currentTime) / MM_SECONDS_PER_DAY);
				if (dayOffSet <= 0) {
					System.out.println("\texpired on "+ cert.getNotAfter());
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
					totalCertificatesDeleted++;
				}
			}
			System.out.println("\n\tTotal certificates removed from the keystore "+Keystore+" : "+totalCertificatesDeleted+"\n");
		} catch (KeyStoreException e1) {
			System.err.println("\tERROR| KeystoreCleaner.java KeystoreException occured.\n");
		}
	}

}