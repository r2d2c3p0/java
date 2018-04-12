package org.r2d2c3p0.simpletext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class KeystorePassword {

	public static String run(String keystoreFile, KeyStore keystore, String encryptedFile) {

		String wPassword=null;

		try {
			@SuppressWarnings("unchecked")
			List<String> lines = FileUtils.readLines(new File(encryptedFile), "utf-8");
			for (int i=0;i<lines.size();i++) {
				try {
					String decrypted_password = BCEncrypterDecrypter.decrypt(lines.get(i), "e0l1p2m3i4s5r6e7v8e9n11d12n13a14e15r16u17p18y19l20e21r22a23r24s25i26h27t28u29r30t31e32h33t34");
					try {
						FileInputStream in1 = new FileInputStream(keystoreFile);
						try {
							keystore.load(in1, decrypted_password.toCharArray());
							wPassword=decrypted_password;
							break;
						} catch (NullPointerException npe) {
							//System.out.println(npe);
						} catch (NoSuchAlgorithmException nsae) {
							//System.out.println(nsae);
						} catch (CertificateException ce) {
							//System.out.println(ce);
						} catch (IOException ioe) {
							//System.out.println(ioe);
						} catch (ExceptionInInitializerError eiie) {
							//System.out.println(eiie);
						} catch (NoClassDefFoundError ncdfe) {
							//System.out.println(ncdfe);
						}
					} catch (FileNotFoundException fnfe) {
						//System.out.println(fnfe);
					}
				} catch (Exception e) {
					System.out.println(" [KeystorePassword] check the encrypted file.\n");
				}
			}
		} catch (IOException e) {
			System.out.println(" [KeystorePassword] IOException (BCEncrypterDecrypter).");
		}

		return wPassword;
	}

}