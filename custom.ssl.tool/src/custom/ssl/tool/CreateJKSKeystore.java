package custom.ssl.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyStore;

public class CreateJKSKeystore {
	
	public static KeyStore createKeyStore(String Keystore, String Password) throws Exception {
	    File file = new File(Keystore);
	    KeyStore keyStore = KeyStore.getInstance("JKS");
	    if (file.exists()) {
	        System.out.println("\tError: "+Keystore+" already exists.");
	        System.exit(1);
	    } else {
	        // if not exists, create
	        keyStore.load(null, null);
	        keyStore.store(new FileOutputStream(file), Password.toCharArray());
	    }
	    return keyStore;
	}
	
	public static void main(String[] args) {
		try {
			createKeyStore(args[0], args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}