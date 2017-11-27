package custom.ssl.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Scanner;

import org.bouncycastle.util.encoders.Base64Encoder;

public class ExtractPrivateKey {

	private static File exportedFile=new File("private.key");;

	@SuppressWarnings("resource")
	public static void ExtractPrivateKeyMain(String Keystore, String Password) throws
			KeyStoreException,
			NoSuchAlgorithmException,
			CertificateException,
			IOException {

		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		FileInputStream in1 = new FileInputStream(Keystore);
		ks.load(in1, Password.toCharArray());
		@SuppressWarnings("rawtypes")
		Enumeration aliasEnumumeration;
		Scanner sc=new Scanner(System.in);

		try {
			aliasEnumumeration = ks.aliases();
			while (aliasEnumumeration.hasMoreElements()) {
				System.out.println();
				String cAlias = (String) aliasEnumumeration.nextElement();
				if (ks.isKeyEntry(cAlias)) {
					System.out.println("\t|*|Alias (PrivateKey): "+cAlias);
					System.out.print("\tDownload private key from the keystore '"+Keystore+"'?: [y/n] ");
					String InputGiven=sc.next();
					if (InputGiven.equals("y")) {
						Base64Encoder encoder=new Base64Encoder();
		                ks.load(new FileInputStream(Keystore), Password.toCharArray());
		                KeyPair keyPair=GetPrivateKey(ks, cAlias, Password);
		                PrivateKey privateKey=keyPair.getPrivate();
		                int encoded=encoder.encode(privateKey.getEncoded(), 0, 0, null);
		                FileWriter fw=new FileWriter(exportedFile);
		                fw.write("--BEGIN PRIVATE KEY--\n");
		                fw.write(encoded);
		                fw.write("\n");
		                fw.write("--END PRIVATE KEY--");
		                fw.close();
						System.out.println("\tKey ["+cAlias+"] downloaded.");
					} else {
						System.out.println("\tKey ["+cAlias+"] will be skipped.");
					}
				}
				System.out.println();
			}
		} catch (KeyStoreException e1) {
			System.out.println("\tERROR| ExtractPrivateKey.java KeystoreException occured.\n");
		}
	}

	public static KeyPair GetPrivateKey(KeyStore keystore, String alias, String password) {
        try {
        	Key key=keystore.getKey(alias, password.toCharArray());
        	if (key instanceof PrivateKey) {
        		Certificate cert=keystore.getCertificate(alias);
        		PublicKey publicKey=cert.getPublicKey();
        		return new KeyPair(publicKey,(PrivateKey)key);
        	}
        } catch (UnrecoverableKeyException e1) {
        	System.out.println("\tERROR| ExtractPrivateKey.java UnrecoverableKeyException occured.\n");
        } catch (NoSuchAlgorithmException e2) {
        	System.out.println("\tERROR| ExtractPrivateKey.java NoSuchAlgorithmException occured.\n");
        } catch (KeyStoreException e3) {
        	System.out.println("\tERROR| ExtractPrivateKey.java KeyStoreException(GetPrivateKey) occured.\n");
        }
        return null;
	}

}