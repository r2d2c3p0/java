package custom.ssl.tool;

/**
 * Not in use
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Scanner;

public class ImportPrivateKey {
	
	@SuppressWarnings({ "rawtypes", "unused", "resource" })
	public static void ImportPrivateKeyMethod(String Keystore, String Password, 
			String KeyFileName, String KeyPassword) throws 
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
		
		try {			
			while (WrongResponse == 0) {
				System.out.println();
				System.out.print("\tEnter alias for the private key "+KeyFileName+" : ");  
				String InputAliasGiven=sc.next();				    
				if (ks.containsAlias(InputAliasGiven)) {
				    System.out.println("\tAlias "+InputAliasGiven+" already present in the keystore.");
				    //return;
				} else {								    
				    WrongResponse=1;				    	
				    System.out.println("\t"+KeyFileName+"[Alias "+InputAliasGiven+"] added to the keystore.");
				 }
			}						
		} catch (KeyStoreException e1) {
			//e1.printStackTrace();
			System.out.println("\tERROR| ImportPrivateKey.java KeystoreException occured.");
		}
		System.out.println();
	}

}