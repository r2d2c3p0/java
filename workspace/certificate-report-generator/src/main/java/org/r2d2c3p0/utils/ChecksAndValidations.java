package org.r2d2c3p0.utils;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;

import org.apache.log4j.Logger;

public class ChecksAndValidations {

	//private final static Logger logger = Logger.getLogger(ChecksAndValidations.class);
	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	public static KeyStore PreChecksAndValidations(String kFilename) throws KeyStoreException {

		logger.debug(" [ChecksAndValidations] start - reading :"+kFilename);
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
					logger.warn(" returned null ("+ kFilename +")");
					logger.debug(" [ChecksAndValidations] end.");
					return null;
				}
			} else if (kFilename.endsWith(".jks") || kFilename.endsWith(".JKS")) {
				ks = KeyStore.getInstance("JKS");
			} else if (kFilename.endsWith(".key")) {
				ks = KeyStore.getInstance("RSA");
			} else {
				logger.error(" [ChecksAndValidations] Keystore ("+kFilename+") type is not supported by this program.\n");
				logger.error(" [ChecksAndValidations] Certificate Report Generator App exited with error.\n");
				System.exit(2);
			}
		}
		logger.debug(" [ChecksAndValidations] end.");
		return ks;
	}

}