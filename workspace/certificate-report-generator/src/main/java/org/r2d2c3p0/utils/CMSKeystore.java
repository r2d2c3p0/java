package org.r2d2c3p0.utils;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;

import org.apache.log4j.Logger;

public class CMSKeystore {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

    public static boolean PrivateKey(String keystore, String alias, String encryptedFile) {

    	logger.debug(" [CMSKeystore] start.");
    	KeyStore keystoreInstance;
		try {
			keystoreInstance = ChecksAndValidations.PreChecksAndValidations(keystore);
			char[] password = KeystorePassword.run(keystore, keystoreInstance,
					encryptedFile).toCharArray();
            try {
                    Key key=keystoreInstance.getKey(alias, password);
                    if (key instanceof PrivateKey) {
                    		logger.debug(" [CMSKeystore] instanceof end.");
                            return true;
                    }
            } catch (UnrecoverableKeyException uke) {
            	logger.error(" [CMSKeystore] "+uke);
            } catch (NoSuchAlgorithmException nsae) {
            	logger.error(" [CMSKeystore] "+nsae);
            } catch (KeyStoreException kse) {
            	logger.error(" [CMSKeystore] [1] "+kse);
            }
		} catch (KeyStoreException kse) {
			logger.error(" [CMSKeystore] [2] "+kse);
		}
		logger.debug(" [CMSKeystore] end.");
		return false;
    }
}