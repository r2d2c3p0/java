package custom.ssl.tool;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;

import org.bouncycastle.openssl.PEMWriter;

public class PrivateKeyMain {
	
	public static void exportPrivateKey(String Keystore, String Alias, String Password) throws Exception {
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		KeyStore keystore = KeyStore.getInstance("PKCS12", "BC");
		keystore.load(new FileInputStream(Keystore), Password.toCharArray());
		KeyPair keyPair = getPrivateKey(keystore, Alias, Password.toCharArray());
		PrivateKey privateKey = keyPair.getPrivate();
		FileWriter fw = new FileWriter("private.pem");
		PEMWriter writer = new PEMWriter(fw);
		writer.writeObject(privateKey);
		writer.close();
	}

	private static KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) {
		try {
			Key key = keystore.getKey(alias, password);
			if (key instanceof PrivateKey) {
				java.security.cert.Certificate cert = keystore
						.getCertificate(alias);
				PublicKey publicKey = cert.getPublicKey();
				return new KeyPair(publicKey, (PrivateKey) key);
			}
		} catch (UnrecoverableKeyException e) {
		} catch (NoSuchAlgorithmException e) {
		} catch (KeyStoreException e) {
		}
		return null;
	}

}