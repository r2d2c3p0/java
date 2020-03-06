package org.r2d2c3p0.cr.generator.html;

import java.net.HttpURLConnection;
import java.net.URL;

import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;

public class CheckInternetConnection {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	public static boolean run() {

		logger.debug(" [CheckInternetConnection] start.");
		HTMLConfig networkConfig = ConfigFactory.create(HTMLConfig.class);
        try {

            URL url = new URL(networkConfig.checkURL());
            int timeOutInMilliSec = networkConfig.urlTimeout();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            logger.debug(" [CheckInternetConnection] timeout="+networkConfig.urlTimeout());
            httpURLConnection.setRequestMethod(networkConfig.requestMethod());
            httpURLConnection.setConnectTimeout(timeOutInMilliSec);
            httpURLConnection.setReadTimeout(timeOutInMilliSec);

            int responseCode = httpURLConnection.getResponseCode();

            if ((200<=responseCode) && (responseCode<=399)) {
            	logger.debug(" [CheckInternetConnection] using CDN.");
            	logger.debug(" [CheckInternetConnection] end.");
                return true;
            }

        } catch (Exception exp) {
        	logger.debug(" [CheckInternetConnection] local only.");
        	logger.debug(" [CheckInternetConnection] end.");
            return false;
        }
		return false;
    }
}