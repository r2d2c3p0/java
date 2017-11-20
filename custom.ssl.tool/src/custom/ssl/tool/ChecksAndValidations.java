package custom.ssl.tool;

import java.security.KeyStoreException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;

public class ChecksAndValidations {
	
	static KeyStore PreChecksAndValidations(String kFilename) throws KeyStoreException {

		File fKeystore = new File(kFilename);
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		if (fKeystore.exists()) {
			if (kFilename.endsWith(".p12") || kFilename.endsWith(".PFX") || kFilename.endsWith(".pfx") 
					|| kFilename.endsWith(".P12")) {
				ks = KeyStore.getInstance("PKCS12");
			} else if (kFilename.endsWith(".kdb") || kFilename.endsWith(".KDB")) {
				try {
					ks = KeyStore.getInstance("CMSKS");
				} catch (Exception e) {
					System.out.println("\tError: use IBM Java Provider. check your java.security.");
					System.exit(11);
				}
			} else if (kFilename.endsWith(".jks") || kFilename.endsWith(".JKS")) {
				ks = KeyStore.getInstance("JKS");
			} else if (kFilename.endsWith(".key")) {
				ks = KeyStore.getInstance("PKCS12");
			} else {
				System.out.println("\n\tERROR| Keystore ("+kFilename+") type is not supported by this program.\n");
				System.exit(2);
			}
		} else {
			System.out.println("\n\tERROR| Keystore "+kFilename+" not found\n");
			System.exit(1);
		}
		return ks;
	}
	
	@SuppressWarnings("resource")
	static InputStream fullStream(String fname) throws IOException {
	    FileInputStream fis = new FileInputStream(fname);
	    DataInputStream dis = new DataInputStream(fis);
	    byte[] bytes = new byte[dis.available()];
	    dis.readFully(bytes);
	    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
	    return bais;
	}

}