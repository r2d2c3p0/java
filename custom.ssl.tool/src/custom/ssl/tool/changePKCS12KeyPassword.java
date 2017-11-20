package custom.ssl.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.io.RandomAccessFile;

public class changePKCS12KeyPassword {

	public static byte[] changePKCS12KeyPasswordMain(String privateKeyData, String oldPassword, String newPassword) throws 
		NoSuchAlgorithmException, 
		CertificateException, 
		IOException {
		
		
		@SuppressWarnings("resource")
		RandomAccessFile f = new RandomAccessFile(privateKeyData, "r");
		byte[] b = new byte[(int)f.length()];
		f.readFully(b);
		
	    try {
	        KeyStore newKs = KeyStore.getInstance("PKCS12");
	        newKs.load(null, null);

	        KeyStore ks = KeyStore.getInstance("PKCS12");
	        ks.load(new ByteArrayInputStream(b), oldPassword.toCharArray());
	        Enumeration<String> aliases = ks.aliases();

	        while (aliases.hasMoreElements()) {
	            String alias = aliases.nextElement();
	            Key privateKey;
				try {
					privateKey = ks.getKey(alias, oldPassword.toCharArray());
					java.security.cert.Certificate[] certificateChain = ks.getCertificateChain(alias);
		            newKs.setKeyEntry(alias, privateKey, newPassword.toCharArray(), certificateChain);
				} catch (UnrecoverableKeyException e) {
					e.printStackTrace();
				}
	            
	        }
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        newKs.store(baos, newPassword.toCharArray());
	        return baos.toByteArray();
	    } catch (KeyStoreException e) {
	        throw new RuntimeException(e);
	    }
	}

}