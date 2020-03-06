package org.r2d2c3p0.cr.generator.json;

import java.io.File;
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
import java.util.List;

import org.apache.log4j.Logger;
import org.r2d2c3p0.utils.CMSKeystore;
import org.r2d2c3p0.utils.ChecksAndValidations;
import org.r2d2c3p0.utils.GetLocalTime;
import org.r2d2c3p0.utils.KeystorePassword;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;

public class GenerateJSON {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");
	private static final long MM_SECONDS_PER_DAY = 1000 * 60 * 60 * 24;

	public static void run(List<String> fileFinalList, int i, String encryptedFile, String outFile) {

		logger.debug(" GenerateJSON start");
		if (!outFile.isEmpty()) {
			try {
				logger.info("JSON output file "+outFile);
				new FileWriter(outFile).close();
			} catch (IOException ioe) {
				logger.debug(ioe);
				logger.debug(" outFile(JSON) reset failed.");
			}
		}

		for (String keystore : fileFinalList) {
			KeyStore keystoreInstance;
			try {
				keystoreInstance = ChecksAndValidations.PreChecksAndValidations(keystore);
				logger.debug("Keystore instance :"+keystoreInstance);
				try {
					logger.info("Working on "+keystore+" ... ");
					keystoreInstance.load(new FileInputStream(keystore),
							KeystorePassword.run(keystore, keystoreInstance, encryptedFile)
							.toCharArray());
					Enumeration<String> aliasEnumumeration = keystoreInstance.aliases();

					while (aliasEnumumeration.hasMoreElements()) {

						try {

							JsonFactory jfactory = new JsonFactory();
							JsonGenerator jGenerator = jfactory.createGenerator(new File(outFile), JsonEncoding.UTF8);
							jGenerator.writeStartObject(); // {
							String cAlias = aliasEnumumeration.nextElement();
							logger.info("Working on "+cAlias+" ... ");
							Certificate[] chainCertificates;

							X509Certificate x509certificate = (X509Certificate) keystoreInstance
									.getCertificate(cAlias);
							jGenerator.writeStringField("Common Name", x509certificate.getSubjectDN().toString());
							jGenerator.writeStringField("Issued By", x509certificate.getIssuerDN().toString());
							jGenerator.writeStringField("Signature Algorithm", x509certificate.getSigAlgName().toString());
							jGenerator.writeStringField("Serial Number", x509certificate.getSerialNumber().toString());
							jGenerator.writeStringField("Serial Number(HEX)", x509certificate.getSerialNumber().toString(16));
							jGenerator.writeStringField("Alias/ Label", cAlias);
							jGenerator.writeStringField("Keystore", keystore);

							Date expTime = x509certificate.getNotAfter();
							long expTimeL = expTime.getTime();
							Date bornTime = x509certificate.getNotBefore();
							long bornTimeL = bornTime.getTime();
							jGenerator.writeStringField("Expires On", GetLocalTime.run(expTimeL));
							jGenerator.writeStringField("Issued On", GetLocalTime.run(bornTimeL));

							int dayOffSet = (int) ((expTimeL - bornTimeL) / MM_SECONDS_PER_DAY);
							String status = "Valid";
							if (dayOffSet <= 0) status = "Expired";
							String pKey = "false";
							if (CMSKeystore.PrivateKey(keystore, cAlias, encryptedFile)) pKey = "true";
							jGenerator.writeStringField("Status", status);
							jGenerator.writeStringField("Private/ Personal Key", pKey);

							jGenerator.writeFieldName("SAN Entries"); // "san" :
							jGenerator.writeStartArray(); // [
							try {
								for (List<?> SAN: x509certificate.getSubjectAlternativeNames()) {
									jGenerator.writeString(SAN.get(1).toString());
								}
							} catch (Exception e1) {
								jGenerator.writeString("");
								logger.debug("No SAN entries found for "+x509certificate.getSubjectDN().toString());
							}
							jGenerator.writeEndArray(); // ]
							if (keystoreInstance.isKeyEntry(cAlias)) {
								jGenerator.writeFieldName("Chain Certificates"); // "chain" :
								jGenerator.writeStartArray(); // [
								chainCertificates = keystoreInstance.getCertificateChain(cAlias);
								for (int ce=1;ce<chainCertificates.length;ce++) {
									X509Certificate certchain = (X509Certificate) chainCertificates[ce];
									jGenerator.writeString(certchain.getSubjectDN().toString());

								}
								jGenerator.writeEndArray(); // ]
							}
							jGenerator.writeEndObject(); // }
							jGenerator.close();
						} catch (JsonGenerationException e) {
							e.printStackTrace();
						} catch (JsonMappingException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

//					if (outFile.isEmpty()) {
//						System.out.println(mapper.writerWithDefaultPrettyPrinter()
//								.writeValueAsString(keystoreObject));
//					} else {
//						@SuppressWarnings("resource")
//						FileWriter file = new FileWriter(outFile, true);
//						file.write(mapper.writerWithDefaultPrettyPrinter()
//								.writeValueAsString(keystoreObject));
//						file.flush();
//					}
//
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