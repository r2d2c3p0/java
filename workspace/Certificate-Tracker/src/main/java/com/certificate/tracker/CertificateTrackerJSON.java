package com.certificate.tracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.certificate.tracker.utils.ChecksAndValidations;
import com.certificate.tracker.utils.GetLocalTime;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CertificateTrackerJSON {

	private static final long MM_SECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	private final static Logger logger = Logger.getLogger(CertificateTrackerJSON.class);

	
	@SuppressWarnings("unchecked")
	public static void run(List<String> keystoreFinalList, String encryptedFile, String outFile) {

		logger.debug(" start");
		if (!outFile.isEmpty()) {
			try {
				logger.info("JSON output file "+outFile);
				new FileWriter(outFile).close();
			} catch (IOException ioe) {
				logger.debug(ioe);
				logger.error("outFile reset failed.");
			} catch (NullPointerException npe) {
				logger.debug(npe);
				logger.error("outFile reset failed. returned null.");
			}
		}
		
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
					JSONArray certificatesArray = new JSONArray();
					JSONObject keystoreObject = new JSONObject();
					keystoreObject.put("Keystore", keystore);
					int certificateNumber=0;
					
					while (aliasEnumumeration.hasMoreElements()) {
						
						//JSONObject certificateSingleObject = new JSONObject();
						
						/*
						 * JSON Object is HaspMap and not ordered.
						 * Alternative is to use LinkedHaspMap and pass it as the argument to JSON Object.
						 * The default data type for JSON Object is HaspMap.
						 * Needed the ordered array output.
						 * Can use Apache Wink (will require this code for just one operation.)
						 * 
						*/
						LinkedHashMap<String, String> certificateSingleObject = new LinkedHashMap<String, String>();
						
						String cAlias = aliasEnumumeration.nextElement();
						Certificate[] chainCertificates;
						
						X509Certificate x509certificate = (X509Certificate) keystoreInstance
								.getCertificate(cAlias);

						certificateSingleObject.put("Common Name",
								x509certificate.getSubjectDN().toString());
						certificateSingleObject.put("Issued By",
								x509certificate.getIssuerDN().toString());
						certificateSingleObject.put("Signature Algorithm",
								x509certificate.getSigAlgName().toString());
						certificateSingleObject.put("Serial Number",
								x509certificate.getSerialNumber().toString());
						certificateSingleObject.put("Serial Number(HEX)",
								x509certificate.getSerialNumber().toString(16));
						certificateSingleObject.put("Alias/ Label", cAlias);
						
						JSONObject orderedCertificateSingleObject = new JSONObject(certificateSingleObject);
						
						Date expTime = x509certificate.getNotAfter();
						long expTimeL = expTime.getTime();
						Date bornTime = x509certificate.getNotBefore();
						long bornTimeL = bornTime.getTime();
						
						orderedCertificateSingleObject.put("Expires On", GetLocalTime.run(expTimeL));
						orderedCertificateSingleObject.put("Issued On", GetLocalTime.run(bornTimeL));
						
						int dayOffSet = (int) ((expTimeL - bornTimeL) / MM_SECONDS_PER_DAY);
						String status = "Valid";
						if (dayOffSet <= 0) status = "Expired";
						orderedCertificateSingleObject.put("Status", status);

						String pKey = "false";
						if (keystoreInstance.isKeyEntry(cAlias)) pKey="true";
						orderedCertificateSingleObject.put("Private/ Personal Key", pKey);
						
						orderedCertificateSingleObject.put("Key Entry Number", 
								++certificateNumber);
						
						JSONArray sanArray = new JSONArray();
						try {
							
							for (List<?> SAN: x509certificate.getSubjectAlternativeNames()) {
								JSONObject sanSingleObject = new JSONObject();
								sanSingleObject.put("SAN Entry", SAN.get(1));
								sanArray.add(sanSingleObject);
							}
							orderedCertificateSingleObject.put("SAN Entries", sanArray);
							
						} catch (Exception e1) {
							orderedCertificateSingleObject.put("SAN Entries", sanArray);
							logger.debug("No SAN entries found for "+x509certificate
									.getSubjectDN().toString());
						}
						
						if (keystoreInstance.isKeyEntry(cAlias)) {
							chainCertificates = keystoreInstance.getCertificateChain(cAlias);
							JSONArray chainArray = new JSONArray();
							
							for (int ce=1;ce<chainCertificates.length;ce++) {
								JSONObject chainSingleObject = new JSONObject();
								X509Certificate certchain = (X509Certificate) chainCertificates[ce];
								chainSingleObject.put("Chained with", certchain.getSubjectDN());
								chainArray.add(chainSingleObject);					
							}
							orderedCertificateSingleObject.put("Chain Certificates", chainArray);

						}
						certificatesArray.add(orderedCertificateSingleObject);
					}

					keystoreObject.put("Certificates", certificatesArray);
					ObjectMapper mapper = new ObjectMapper();
					
					if (outFile.isEmpty()) {
						System.out.println(mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(keystoreObject));
					} else {
						@SuppressWarnings("resource")
						FileWriter file = new FileWriter(outFile, true);
						file.write(mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(keystoreObject));
						file.flush();
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
		logger.info("Certificate Tracker completed the JSON report.");
		logger.info("Please check the file: "+outFile);
		logger.debug(" end");
	}
	
}