package custom.ssl.tool;

import org.apache.commons.io.FileUtils;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

class KeystorePassword implements Runnable {
	
	private String Password;
	private KeyStore Keystore;
	private String keystore;

	public String getPassword() { 
		return Password;
	}
	public void setPassword(String password) { 
		Password = password;
	}
	public String getkeystore() { 
		return keystore; 
	}
	public void setkeystore(String keystore1) { 
		keystore = keystore1; 
	}
	public KeyStore getKeystore() { 
		return Keystore; 
	}
	public void setKeystore(KeyStore ks) { 
		Keystore = ks; 
	}	
	
	public KeystorePassword(KeyStore ks, String Password, String keystore) {
		
    	this.setPassword(Password);
        this.setKeystore(ks);
        this.setkeystore(keystore);
        
    }
	
	public void run() {
		
    	try {
			FileInputStream in1 = new FileInputStream(keystore);
			try {
				Keystore.load(in1, Password.toCharArray());
				System.out.println("\n\tKeystore "+keystore+" password= "+Password+"\n");					
			} catch (NoSuchAlgorithmException e) {
				//System.exit(2);
				//System.out.println("\tKeystorePassword.run NoSuchAlgorithmException.\n");
			} catch (CertificateException e) {
				//System.exit(2);
				//System.out.println("\tKeystorePassword.run CertificateException.\n");
			} catch (IOException e) {
				//System.exit(2);
				//System.out.println("\tKeystorePassword.run IOException.\n");
			}
		} catch (FileNotFoundException e) {
			//System.out.println("\n\tError: "+Keystore+" not found.\n");
			//System.exit(1);
		}   
    	
    }	
	
	public static void main(String[] args) {
		
		File file = new File(args[0]);
		if (!file.exists()) {
			System.out.println("\n\tError: "+args[0]+" not found.\n");
			System.exit(1);
		}
		
		KeyStore ks;
		try {
			ks = ChecksAndValidations.PreChecksAndValidations(args[0]);
			ExecutorService executor = Executors.newCachedThreadPool();
			try {
				List<String> lines = FileUtils.readLines(new File("encrypted.file"), "utf-8");
				for (int i=0;i<lines.size();i++) {
					String decrypted_password;
					try {
						decrypted_password = BCEncrypterDecrypter.decrypt(lines.get(i), "opalou913");
						executor.execute(new KeystorePassword(ks, decrypted_password, args[0]));
					} catch (Exception e) {
						//e.printStackTrace();
						System.out.println("\tKeystorePassword.main Exception.\n");
					}
				}
			} catch (IOException e) {
				//e.printStackTrace();
				System.out.println("\tKeystorePassword.main IOException.\n");
			}
			executor.shutdownNow();
		} catch (KeyStoreException e1) {
			//e1.printStackTrace();
			System.out.println("\tKeystorePassword.main KeyStoreException.\n");
		}
		
	}
	
}
