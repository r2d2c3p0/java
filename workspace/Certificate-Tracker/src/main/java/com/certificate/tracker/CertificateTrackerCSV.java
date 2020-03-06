package com.certificate.tracker;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;

import com.certificate.tracker.utils.ChecksAndValidations;
import com.certificate.tracker.utils.GetLocalTime;

public class CertificateTrackerCSV {

	private final static Logger logger = Logger.getLogger(CertificateTrackerCSV.class);
	
	public static void run(List<String> keystoreFinalList, String encryptedFile, String outFile) {

		logger.debug(" start");
		if (!outFile.isEmpty()) {
			try {
				logger.info("CSV output file "+outFile);
				new FileWriter(outFile).close();
			} catch (IOException e) {
				logger.debug(e);
				logger.debug("outFile reset failed.");
			}
		}
		
	    BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(Paths.get(outFile));
			CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader(
                    		"Keystore",
                    		"Alias",
                    		"Common Name", 
                    		"Expiration",
                    		"Serial Number(HEX)",
                    		"Serial Number",
                    		"Issuer"));
			logger.info(csvPrinter);
			for (String keystore : keystoreFinalList) {
				KeyStore keystoreInstance;
				try {
					keystoreInstance = ChecksAndValidations.PreChecksAndValidations(keystore);
					logger.debug("Keystore instance :"+keystoreInstance);
					try {
							keystoreInstance.load(new FileInputStream(keystore),
									KeystorePassword.run(keystore, keystoreInstance, encryptedFile)
									.toCharArray());
							Enumeration<String> aliasEnumumeration = keystoreInstance.aliases();
							while (aliasEnumumeration.hasMoreElements()) {
								String cAlias = aliasEnumumeration.nextElement();
								
								X509Certificate x509certificate = (X509Certificate) keystoreInstance
										.getCertificate(cAlias);
								Date expTime = x509certificate.getNotAfter();
								long expTimeL = expTime.getTime();
								csvPrinter.printRecord(keystore, cAlias, 
										x509certificate.getSubjectDN().toString(),
										GetLocalTime.run(expTimeL), 
										x509certificate.getSerialNumber().toString(16),
										x509certificate.getSerialNumber().toString(),
										x509certificate.getIssuerDN());
							}
					} catch (NoSuchAlgorithmException nsae) {
						logger.debug(nsae);
					} catch (CertificateException ce) {
						logger.debug(ce);
					} catch (FileNotFoundException fnfe) {
						logger.debug(fnfe);
					} catch (IOException ioe) {
						logger.debug(ioe);
					} catch (NullPointerException npe) {
						logger.debug(npe);
					}
					
				} catch (KeyStoreException kse) {
					logger.error("KeyStoreException occured");
					logger.debug(kse);
				}
			}
			logger.debug(" csv printer flush completed.");
			csvPrinter.flush();
		} catch (IOException ioe) {
			logger.debug(ioe);
		}
		logger.info("Certificate Tracker completed the CSV report.");
		logger.info("Please check the file: "+outFile);
		logger.debug(" end");
		
	}

}