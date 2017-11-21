package custom.ssl.tool;

import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;

public class Password {

	public static void main(String[] args) throws FileNotFoundException {

		KeyStore ks;
		File file = new File(args[0]);
		if (!file.exists()) {
			System.out.println("\n\tERROR| "+args[0]+" not found.\n");
			System.exit(1);
		} else {
			try {
				ks = ChecksAndValidations.PreChecksAndValidations(args[0]);
				FileInputStream in1 = new FileInputStream(args[0]);
				try {
					ks.load(in1, args[1].toCharArray());
					System.out.println(args[1]);
					System.exit(0);
				} catch (NoSuchAlgorithmException e) {
					//e.printStackTrace();
					System.exit(2);
				} catch (CertificateException e) {
					//e.printStackTrace();
					System.exit(2);
				} catch (IOException e) {
					//e.printStackTrace();
					System.exit(2);
				}
			} catch (KeyStoreException e) {
				//e.printStackTrace();
				System.exit(1);
			}
		}
	}

}