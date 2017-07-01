package encryption;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.io.File;
import java.io.FileOutputStream;

public class GenerateKeys {

	private KeyPairGenerator keyGen;
	private KeyPair pair;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public GenerateKeys(int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
		this.keyGen = KeyPairGenerator.getInstance("RSA");
		this.keyGen.initialize(keylength);
	}

	public void createKeys() {
		this.pair = this.keyGen.generateKeyPair();
		this.privateKey = pair.getPrivate();
		this.publicKey = pair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public void writeToFile(String path, byte[] key) throws IOException {
		File keyfile = new File(path);
		keyfile.getParentFile().mkdirs();

		FileOutputStream keyfileos = new FileOutputStream(keyfile);
		keyfileos.write(key);
		keyfileos.flush();
		keyfileos.close();
	}

	public static void main(String[] args) {
		
		GenerateKeys genkey;
		
		try {
			genkey = new GenerateKeys(1024);
			genkey.createKeys();
			genkey.writeToFile("RSAKeys/public.key", genkey.getPublicKey().getEncoded());
			genkey.writeToFile("RSAKeys/private.key", genkey.getPrivateKey().getEncoded());
		} catch (NoSuchAlgorithmException nsae) {
			System.err.println(nsae.getMessage());
		} catch (NoSuchProviderException nspe) {
			System.err.println(nspe.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

	}

}