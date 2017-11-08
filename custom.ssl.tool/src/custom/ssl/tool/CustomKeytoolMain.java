package custom.ssl.tool;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;

public class CustomKeytoolMain {

	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, IOException {

		String Keystore = args[0];
		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		Scanner Operation = new Scanner(System.in);

		String Password = PasswordField.readPassword("\n\tEnter keystore '"+Keystore+"' password: ");

		System.out.println("\n\t\t============================================");
		System.out.println("\t\t Keystore ["+Keystore+"], Select operation below:");
		System.out.println("\t\t============================================");
		System.out.println("\t\t 1. List Certificates\n\t\t 2. Add Certificates\n\t\t 3. "
				+ "Delete Certificates\n\t\t 4. Add Personal/Private Key");
		System.out.println("\t\t============================================\n\n");
		
		String Selection;
		System.out.print("\tMake your selection [1/2/3/4]: ");
		Selection = Operation.next();

		if (Selection.equals("1")) {
			System.out.println();
			try {
				ListCertificates.ListCertificatesMain(Keystore, Password);
			} catch (KeyStoreException e) {
				//e.printStackTrace();
				System.out.println("\tERROR| KeystoreException (keystore corrupted) occured.");
			} catch (NoSuchAlgorithmException e) {
				//e.printStackTrace();
				System.out.println("\tERROR| NoSuchAlgorithmException (JKS/P12/KDB only) occured.");
			} catch (CertificateException e) {
				//e.printStackTrace();
				System.out.println("\tERROR| CertificateException (certificate is invalid) occured.");
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("\tERROR| IOException (password incorrect??) occured.");
			}			
		} else if (Selection.equals("2")) {
			File cacertificates = new File("CACertificates");			
			if (cacertificates.exists() && cacertificates.isDirectory()) {
				File[] certificates = new File("CACertificates").listFiles();
				for (File certificate : certificates) {					
					if (certificate.isDirectory()) {
						int x=0;						
					} else {						
						if (certificate.toString().endsWith(".cer") || certificate.toString().endsWith(".crt")) {
							System.out.println("\tAdding certificate: " + certificate.getName());
							String certfile = certificate.getName();												
							try {								
								ImportCertificate.ImportCertificateMethod(Keystore, Password, "CACertificates/"+certfile);
							} catch (CertificateException e) {								
								System.out.println("\tCertificate: " + certificate.getName()+" is not a valid certificate.\n");
							}
						}						
					}
				}
			} else {
				System.out.println("\tError| 'CACertificates' missing.\n");
				System.exit(1);
			}
		} else if (Selection.equals("3")) {
			System.out.println();
			try {
				DeleteCertificate.DeleteCertificatesMain(Keystore, Password);
			} catch (CertificateException e) {
				//e.printStackTrace();
				System.out.println("\tERROR| CertificateException occured.");
			}
		} else if (Selection.equals("4")) {
			System.out.println();
			String Keyfile;
			System.out.print("\tEnter Keyfile location: ");
			Keyfile = Operation.next();
			String KeyPassword = PasswordField.readPassword("\n\tEnter keyfile '"+Keyfile+"' password: ");
			try {
				ImportPrivateKey.ImportPrivateKeyMethod(Keystore, Password, Keyfile, KeyPassword);
			} catch (CertificateException e) {
				//e.printStackTrace();
				System.out.println("\tERROR| CertificateException occured.");
			}
		} else {			
			System.out.println("\t\tTry again.\n");
		}
	}

}