package org.r2d2c3p0.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;


public class KeystorePassword {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	public static String run(String keystoreFile, KeyStore keystore, String encryptedFile) {

		logger.debug(" [KeystorePassword] start.");
		String wPassword=null;

		try {
			List<String> lines = FileUtils.readLines(new File(encryptedFile), "utf-8");
			for (int i=0;i<lines.size();i++) {
				try {
					String decrypted_password = BCEncrypterDecrypter.decrypt(lines.get(i), "xxxx");
					try {
						FileInputStream in1 = new FileInputStream(keystoreFile);
						try {
							keystore.load(in1, decrypted_password.toCharArray());
							wPassword=decrypted_password;
							logger.debug(" [KeystorePassword] password decrypted for :"+keystoreFile);
							break;
						} catch (NullPointerException npe) {
							//logger.error(npe);
						} catch (NoSuchAlgorithmException nsae) {
							//logger.error(nsae);
						} catch (CertificateException ce) {
							//logger.error(ce);
						} catch (IOException ioe) {
							//logger.error(ioe);
						} catch (ExceptionInInitializerError eiie) {
							//logger.error(eiie);
						} catch (NoClassDefFoundError ncdfe) {
							//logger.error(ncdfe);
						}
					} catch (FileNotFoundException fnfe) {
						//logger.error(fnfe);
					}
				} catch (Exception e) {
					logger.error(" [KeystorePassword] check the encrypted file.\n");
				}
			}
		} catch (IOException e) {
			logger.error(" [KeystorePassword] IOException (BCEncrypterDecrypter).");
		}

		logger.debug(" [KeystorePassword] end.");
		return wPassword;
	}

}