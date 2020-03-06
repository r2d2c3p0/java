package custom.ssl.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.Scanner;

import custom.ssl.util.PasswordField;

public class CreateJKSKeystore {

	public static KeyStore createKeyStore(String Keystore, String Password) throws Exception {
	    File file = new File(Keystore);
	    KeyStore keyStore = KeyStore.getInstance("JKS");
	    if (file.exists()) {
	        System.out.println("\n\tError: "+Keystore+" already exists.\n");
	        System.exit(1);
	    } else {
	        keyStore.load(null, null);
	        keyStore.store(new FileOutputStream(file), Password.toCharArray());
	    }
	    return keyStore;
	}

	public static String inputPassword1() {
		//@SuppressWarnings("resource")
		String Password1 = PasswordField.readPassword("\tEnter new password: ");
		/*Scanner scanner=new Scanner(System.in);
		System.out.print("\tEnter new password: ");
		String Password1=scanner.next();
		*/
		return Password1;
	}

	public static String inputPassword2() {
		//@SuppressWarnings("resource")
		String Password2 = PasswordField.readPassword("\tEnter password again: ");
		/*Scanner scanner=new Scanner(System.in);
		System.out.print("\tEnter password again: ");
		String Password2=scanner.next();
		*/
		return Password2;
	}

	public static void CreateJKSKeystoreMain() {
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		System.out.print("\tEnter keystore name: ");
		String InputGiven=sc.next();
		int PasswordTries=1;
		while (PasswordTries<4) {
			String p1=inputPassword1();
			String p2=inputPassword2();
			if (p1.equals(p2)) {
				try {
					createKeyStore(InputGiven, p1);
					System.out.println("\n\tKeystore(JKS) "+InputGiven+" created successfully.\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			} else {
				PasswordTries++;
			}
		}
		if (PasswordTries>3) {
			System.out.println("\n\tMaximum allowed passwords attempts reached.");
			System.out.println("\tError: creating the keystore '"+InputGiven+"'.\n");
			System.exit(1);
		}
	}

}