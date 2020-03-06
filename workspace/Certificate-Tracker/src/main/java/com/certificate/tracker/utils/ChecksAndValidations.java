package com.certificate.tracker.utils;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;

import org.apache.log4j.Logger;

public class ChecksAndValidations {
	
	private final static Logger logger = Logger.getLogger(ChecksAndValidations.class);
	
	public static KeyStore PreChecksAndValidations(String kFilename) throws KeyStoreException {

		logger.debug("reading :"+kFilename);
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
					logger.warn("Use IBM Java Provider with GSK Toolkit. check your java.security.");
					logger.warn("returned null ("+ kFilename +")");
					return null;
				}
			} else if (kFilename.endsWith(".jks") || kFilename.endsWith(".JKS")) {
				ks = KeyStore.getInstance("JKS");
			} else if (kFilename.endsWith(".key")) {
				ks = KeyStore.getInstance("RSA");
			} else {
				logger.error("Keystore ("+kFilename+") type is not supported by this program.\n");
				logger.error("Certificate-Tracker App exited with error.\n");
				System.exit(2);
			}
		}
		return ks;
	}

}