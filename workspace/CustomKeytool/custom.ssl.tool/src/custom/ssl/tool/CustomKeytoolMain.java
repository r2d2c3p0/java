package custom.ssl.tool;

/**
*
*
* Custom Keytool Main Program.
*
* 11/8/2017.
* 
* Version 2.2 12/4/2017. 
* Version 2.2 update: added properties file to provide customkeytool.properties for key/values pairs.
* 
* JAVA keytool command is not very user friendly, had to come up with this program.
* Dependencies, JAVA 5 or more and Bouncy Castle 1.57, commons-io-2.1 and commons-codec-1.2 jars.
*
* 				==================================================================================
                 	Keystore [key.jks], Select operation below:
                ==================================================================================
                         1. List Certificates                    2. Add Certificates
                         3. Delete Certificates                  4. Extract Certificates
                         5. Import Keystore                      6. Extract Private Key
                         7. Show Signers Information             8. Change PKCS12 Key Password
                ==================================================================================


        Make your selection [1/..../8]:

	If no input keystore is provided, there are two options to choose:
	
		Warning: null keystore
		
				==================================================================================
                 	Keystore [null], Select operation below:
                ==================================================================================
                     a. Create PKCS12 Keystore                    b. List Security Providers
                     c. Create JKS Keystore
                ==================================================================================
*
*
*
*/

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;

import custom.ssl.util.PasswordField;
import custom.ssl.util.GetSecurityProviders;
import custom.ssl.util.ChecksAndValidations;
import custom.ssl.util.GetConfiguration;

public class CustomKeytoolMain {

	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws
		KeyStoreException,
		NoSuchAlgorithmException,
		IOException {

		String CACertificatesPath = null;
		String Keystore = null;
		String Password = null;

        try {
        	Keystore = args[0];
        } catch (ArrayIndexOutOfBoundsException exception) {
        	System.out.println("\n\tWarning: null Keystore.");
        	System.out.println("\n\t\t===================================================="
    				+ "==============================");
    		System.out.println("\t\t\t Keystore [null], Select operation below:");
    		System.out.println("\t\t======================================================"
    				+ "============================");
    		System.out.println("\t\t\t"
    				+ "a. Create PKCS12 Keystore\t\t"
    				+ "b. List Security Providers\n\t\t\t"
    				+ "c. Create JKS Keystore"
    				+ "");
    		System.out.println("\t\t======================================================="
    				+ "===========================\n\n");
    		String Selection1;
    		System.out.print("\tMake your selection [a/b/c]: ");
    		Scanner Operation1 = new Scanner(System.in);
    		Selection1 = Operation1.next();
    		if (Selection1.equals("a")) {
    			System.out.println();
    			System.out.println("\t\tlaunching customkeytool shell script...\n");
    			System.exit(8);
    		} else if (Selection1.equals("b")) {
    			System.out.println();
    			try {
    				GetSecurityProviders.GetProviders1();
    				GetSecurityProviders.GetProviders2();
    			} catch (Exception e) {
    				System.out.println("\tERROR| Exception (GetSecurityProviders.main) occured.\n");
    			}
    		} else if (Selection1.equals("c")) {
    			System.out.println();
    			CreateJKSKeystore.CreateJKSKeystoreMain();    			
    		} else {
    			System.out.println("\t\tSelection not found.\n");
    		}
        	System.exit(1);
        }

		if (Keystore.endsWith(".cer") || Keystore.endsWith(".pem") || Keystore.endsWith(".crt") || Keystore.endsWith(".txt")
		|| Keystore.endsWith(".CER") || Keystore.endsWith(".PEM") || Keystore.endsWith(".CRT") || Keystore.endsWith(".TXT")) {
			try {
				CertificateDetails.CertificateAllDetails(Keystore);
			} catch (CertificateException e) {
				System.out.println("\tERROR| CertificateException (certificate is invalid) occured.\n");
				System.exit(1);
			}
		}

		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		Scanner Operation2 = new Scanner(System.in);
		Password = PasswordField.readPassword("\n\tEnter keystore '"+Keystore+"' password: ");

		System.out.println("\n\t\t===================================================="
				+ "==============================");
		System.out.println("\t\t\t Keystore ["+Keystore+"], Select operation below:");
		System.out.println("\t\t======================================================"
				+ "============================");
		System.out.println("\t\t\t"
				+ "1. List Certificates\t\t\t"
				+ "2. Add Certificates\n\t\t\t"
				+ "3. Delete Certificates\t\t\t"
				+ "4. Extract Certificates\n\t\t\t"
				+ "5. Import Keystore\t\t\t"
				+ "6. Extract Private Key\n\t\t\t"
				+ "7. Show Signers Information\t\t"
				+ "8. Change PKCS12 Key Password"
				+ "");
		System.out.println("\t\t======================================================="
				+ "===========================\n\n");

		String Selection2;System.out.print("\tMake your selection [1/..../8]: ");
		Selection2 = Operation2.next();

		if (Selection2.equals("1")) {
			System.out.println();
			try {
				ListCertificates.ListCertificatesMain(Keystore, Password);
			} catch (KeyStoreException e) {
				System.out.println("\tERROR| KeystoreException (keystore corrupted) occured.\n");
			} catch (NoSuchAlgorithmException e) {
				System.out.println("\tERROR| NoSuchAlgorithmException (JKS/P12/KDB only) occured.\n");
			} catch (CertificateException e) {
				System.out.println("\tERROR| CertificateException (certificate is invalid) occured.\n");
			} catch (IOException e) {
				System.out.println("\tERROR| IOException (password incorrect??) occured.\n");
			}
		} else if (Selection2.equals("2")) {

			try {
				CACertificatesPath = args[1];
	        } catch (ArrayIndexOutOfBoundsException exception) {
	        	CACertificatesPath = GetConfiguration.getCADirectory();
	        	//System.out.println("null CA path.");
	        	//System.exit(1);
	        }

			System.out.println();
			File cacertificates = new File(CACertificatesPath);
			if (cacertificates.exists() && cacertificates.isDirectory()) {
				File[] certificates = new File(CACertificatesPath).listFiles();
				for (File certificate : certificates) {
					if (certificate.isDirectory()) {
						int x=0;
					} else {
						if (certificate.toString().endsWith(".cer") || certificate.toString().endsWith(".CER") ||
								certificate.toString().endsWith(".crt") || certificate.toString().endsWith(".CRT") ||
								certificate.toString().endsWith(".txt") || certificate.toString().endsWith(".TXT") ||
								certificate.toString().endsWith(".pem") || certificate.toString().endsWith(".PEM")) {
							System.out.println("\tAdding certificate: " + certificate.getName());
							String certfile = certificate.getName();
							try {
								ImportCertificates.ImportCertificatesMethod(Keystore, Password, CACertificatesPath+"/"+certfile);
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
		} else if (Selection2.equals("3")) {
			System.out.println();
			try {
				DeleteCertificates.DeleteCertificatesMain(Keystore, Password);
			} catch (CertificateException e) {
				System.out.println("\tERROR| CertificateException (Delete.main) occured.\n");
			}
		} else if (Selection2.equals("4")) {
			System.out.println();
			try {
				ExtractCertificates.ExtractCertificatesMain(Keystore, Password);
			} catch (CertificateException e) {
				System.out.println("\tERROR| CertificateException (Extract.main) occured.\n");
			}
		} else if (Selection2.equals("5")) {
			System.out.println();
			System.out.print("\tEnter input keystore location: ");
			String Keyfile = Operation2.next();
			String KeyPassword=PasswordField.readPassword("\n\tEnter '"+Keyfile+"' password: ");
			try {
				KeystoreImport.KeystoreImportMain(Keyfile, Keystore, KeyPassword, Password);
			} catch (Exception e) {
				System.out.println("\tERROR| KeystoreImport (main) occured.\n");
			}
		} else if (Selection2.equals("6")) {
			System.out.println();
			try {
				ExtractPrivateKey.ExtractPrivateKeyMain(Keystore, Password);
			} catch (CertificateException e) {
				System.out.println("\tERROR| CertificateException (pKey.main) occured.\n");
			}
		} else if (Selection2.equals("7")) {
			System.out.println();
			try {
				GetCASigners.GetCASignersMain(Keystore, Password);
			} catch (Exception e) {
				System.out.println("\tERROR| Exception (GetCASigners.main) occured.\n");
			}
		} else if (Selection2.equals("8")) {
			/*
			int PasswordTries=1;
			String PKCS12KeyOldPassword =null;String PKCS12KeyNewPassword =null;
			String PKCS12KeyNewPassword1 =null;
			PKCS12KeyOldPassword = PasswordField.readPassword("\n\tEnter key '"+Keystore+"' password: ");
			System.out.println();
			while (PasswordTries<4) {
				PKCS12KeyNewPassword = PasswordField.readPassword("\n\tEnter key '"+Keystore+"' new password: ");
				PKCS12KeyNewPassword1 = PasswordField.readPassword("\n\tEnter new password again: ");
				if (PKCS12KeyNewPassword.equals(PKCS12KeyNewPassword1)) {
					try {
						ChangePKCS12KeyPassword.changePKCS12KeyPasswordMain(Keystore, PKCS12KeyNewPassword1, PKCS12KeyNewPassword1);
					} catch (CertificateException e) {
						//e.printStackTrace();
						System.out.println("\tNot a valid RSA key.");
						System.exit(1);
					}
					PasswordTries=5;
				} else {
					System.out.println("\tPasswords does not match, try again.");
					PasswordTries++;
				}
			}
			*/
			System.out.println("\t\toperation under construction.\n");
		} else {
			System.out.println("\t\tSelection not found.\n");
			System.exit(1);
		}
	}

}