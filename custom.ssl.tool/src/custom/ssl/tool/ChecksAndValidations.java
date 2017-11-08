package custom.ssl.tool;

import java.security.KeyStoreException;
import java.io.File;
import java.security.KeyStore;

public class ChecksAndValidations {
	
	static KeyStore PreChecksAndValidations(String kFilename) throws KeyStoreException {

		File fKeystore = new File(kFilename);
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		if (fKeystore.exists()) {
			if (kFilename.endsWith(".p12") || kFilename.endsWith(".PFX") || kFilename.endsWith(".pfx") || kFilename.endsWith(".P12")) {
				ks = KeyStore.getInstance("PKCS12");
			} else if (kFilename.endsWith(".kdb") || kFilename.endsWith(".KDB")) {
				ks = KeyStore.getInstance(KeyStore.getDefaultType());
			} else if (kFilename.endsWith(".jks") || kFilename.endsWith(".JKS")) {
				ks = KeyStore.getInstance("JKS");
			} else {
				System.out.println("\n\tERROR| Keystore type is not supported by this program.\n");
				System.exit(2);
			}
		} else {
			System.out.println("\n\tERROR| Keystore "+kFilename+" not found\n");
			System.exit(1);
		}
		return ks;
	}

}