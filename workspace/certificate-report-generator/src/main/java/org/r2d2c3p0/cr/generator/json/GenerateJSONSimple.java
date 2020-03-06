package org.r2d2c3p0.cr.generator.json;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.r2d2c3p0.cr.generator.csv.CertificateDetails;
import org.r2d2c3p0.utils.CMSKeystore;
import org.r2d2c3p0.utils.ChecksAndValidations;
import org.r2d2c3p0.utils.GetLocalTime;
import org.r2d2c3p0.utils.KeystorePassword;

import com.fasterxml.jackson.core.JsonGenerationException;

public class GenerateJSONSimple {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");
	private static final long MM_SECONDS_PER_DAY = 1000 * 60 * 60 * 24;

	private static final Date curDate = new Date();
	private static final long curTime = curDate.getTime();

	@SuppressWarnings("unchecked")
	public static void run(List<String> fileFinalList, int i, String encryptedFile, String outFile) throws JsonGenerationException {

		logger.debug(" [GenerateJSONSimple] start");
		logger.info("JSON output file "+outFile);

		String finalString="{"
				+ "\"Serial Number(HEX)\":\"Certificate Serial Number in Hexadecimal\","
				+ "\"Keystore\":\"Keystore where the certificate is present\","
				+ "\"Status\":\"Expired or Valid\","
				+ "\"Signature Algorithm\":\"Signature Algorithm for the certificate\","
				+ "\"Alias(Label)\":\"Keystore Alias\","
				+ "\"Common Name\":\"Common Name\","
				+ "\"Expires On\":\"Expiration Date\","
				+ "\"Private(Personal) Key\":\"Identifies the key type\","
				+ "\"SAN Entries\":[\"SAN Entries\"],"
				+ "\"Issued By\":\"Issuing Authority\","
				+ "\"Serial Number\":\"Certificate Serial Number in Decimal\","
				+ "\"Issued On\":\"Issued Date\"}";

		if (i==0) {
			logger.info("JSON report for keystore types.");
			for (String keystore : fileFinalList) {
				KeyStore keystoreInstance;
				try {
					logger.info("Working on "+keystore+" ... ");
					keystoreInstance = ChecksAndValidations.PreChecksAndValidations(keystore);
					logger.debug(" [GenerateJSONSimple] Keystore instance :"+keystoreInstance);
					try {

						keystoreInstance.load(new FileInputStream(keystore),
								KeystorePassword.run(keystore, keystoreInstance, encryptedFile)
								.toCharArray());
						Enumeration<String> aliasEnumeration = keystoreInstance.aliases();
						logger.debug(" [GenerateJSONSimple] "+keystore+" loaded successfully.");

						int certificateCount=0;

						while (aliasEnumeration.hasMoreElements()) {

							certificateCount++;JSONObject obj = new JSONObject();
							String cAlias = aliasEnumeration.nextElement();
							logger.info("\t\tWorking on "+cAlias+" ... ");
							Certificate[] chainCertificates;
							logger.debug(" [GenerateJSONSimple] parsing "+cAlias);
							X509Certificate x509certificate =
									(X509Certificate) keystoreInstance
									.getCertificate(cAlias);
							logger.debug(" [GenerateJSONSimple] x509 for "+cAlias+" generated.");

							obj.put("Common Name", x509certificate.getSubjectDN().toString());
							obj.put("Issued By", x509certificate.getIssuerDN().toString());
							obj.put("Signature Algorithm", x509certificate.getSigAlgName().toString());
							obj.put("Serial Number", x509certificate.getSerialNumber().toString());
							obj.put("Serial Number(HEX)", x509certificate.getSerialNumber().toString(16));
							obj.put("Alias(Label)", cAlias);
							obj.put("Keystore", keystore);

							Date expTime = x509certificate.getNotAfter();
							long expTimeL = expTime.getTime();
							Date bornTime = x509certificate.getNotBefore();
							long bornTimeL = bornTime.getTime();
							obj.put("Expires On", GetLocalTime.run(expTimeL));
							obj.put("Issued On", GetLocalTime.run(bornTimeL));

							int dayOffSet = (int) ((expTimeL - curTime) / MM_SECONDS_PER_DAY);
							String status = "Valid";

							if (dayOffSet <= 0) {
								status = "Expired";
					        } else {
					        	if (dayOffSet < 15) {
					        		status = "Expires in less than 15 Days";
					        	} else {
					        		if (dayOffSet < 30) {
					        			status = "Expires in less than 30 Days";
					        		} else {
					        			if (dayOffSet < 60) {
					        				status = "Expires in less than 60 Days";
					        			} else {
					        				if (dayOffSet < 90) {
					        					status = "Expires in less than 90 Days";
					        				} else {
					        					status = "Valid";
					        				}
					        			}
					        		}
					        	}
					        }

							String pKey = "false";
							if (CMSKeystore.PrivateKey(keystore, cAlias, encryptedFile)) pKey = "true";
							obj.put("Status", status);
							obj.put("Private(Personal) Key", pKey);

							JSONArray list = new JSONArray();
							try {
								logger.debug(" [GenerateJSONSimple] san loop for "+cAlias);
								for (List<?> SAN: x509certificate.getSubjectAlternativeNames()) {
									list.add(SAN.get(1).toString());
								}
							} catch (Exception e1) {
								list.add("");
								logger.debug(" [GenerateJSONSimple] no SAN entries found for "+cAlias);
							}
							obj.put("SAN Entries", list);; // ]
							JSONArray clist = new JSONArray();
							if (keystoreInstance.isKeyEntry(cAlias)) {
								logger.debug(" [GenerateJSONSimple] cc loop for "+cAlias);
								chainCertificates = keystoreInstance.getCertificateChain(cAlias);
								for (int ce=1;ce<chainCertificates.length;ce++) {
									X509Certificate certchain = (X509Certificate) chainCertificates[ce];
									clist.add(certchain.getSubjectDN().toString());

								}
								obj.put("Chained Certificates", clist); // ]
							}
							logger.debug(" [GenerateJSONSimple] final string completed for "+cAlias);
							finalString=finalString+"\n"+obj.toJSONString().replace("\\","");

						}
						logger.debug(" [GenerateJSONSimple] "+keystore+" has "+certificateCount+" entries");
					} catch (NoSuchAlgorithmException nsae) {
						logger.error(" [GenerateJSONSimple] "+nsae+" "+keystore);
					} catch (CertificateException ce) {
						logger.error(" [GenerateJSONSimple] "+ce+" "+keystore);
					} catch (FileNotFoundException fnfe) {
						logger.error(" [GenerateJSONSimple] "+fnfe+" "+keystore);
					} catch (IOException ioe) {
						logger.error(" [GenerateJSONSimple] "+ioe+" "+keystore);
					} catch (NullPointerException npe) {
						logger.error(" [GenerateJSONSimple] "+npe+" "+keystore);
					}

				} catch (KeyStoreException kse) {
					logger.error(" [GenerateJSONSimple] KeyStoreException occured "+keystore);
					logger.debug(" [GenerateJSONSimple] "+kse);
				}
			}
		}

		if (i==1) {
			logger.info("JSON report for certificate types.");
			for (String certificate : fileFinalList) {
				logger.info("Working on "+certificate+" ... ");
				try {

					JSONObject objC = new JSONObject();
					CertificateDetails cd = new CertificateDetails(certificate);
					Date expTime = cd.getExpiration();
					long expTimeL = expTime.getTime();
					Date bornTime = cd.getIssued();
					long bornTimeL = bornTime.getTime();

					int dayOffSet = (int) ((expTimeL - curTime) / MM_SECONDS_PER_DAY);

					String status = "Valid";

					if (dayOffSet <= 0) {
						status = "Expired";
			        } else {
			        	if (dayOffSet < 15) {
			        		status = "Expires in less than 15 Days";
			        	} else {
			        		if (dayOffSet < 30) {
			        			status = "Expires in less than 30 Days";
			        		} else {
			        			if (dayOffSet < 60) {
			        				status = "Expires in less than 60 Days";
			        			} else {
			        				if (dayOffSet < 90) {
			        					status = "Expires in less than 90 Days";
			        				} else {
			        					status = "Valid";
			        				}
			        			}
			        		}
			        	}
			        }

					String pKey = "N/A";

					Collection<List<?>> san;
					List<String> clist = new ArrayList<String>();
					try {
						san = cd.getSAN();
						for (List<?> SAN: san) {
							clist.add((String) SAN.get(1));
						}
					} catch (NullPointerException npe) {
						san=null;
					}

					objC.put("Common Name", cd.getCommonName().toString().replace("\"", ""));
					objC.put("Issued By", cd.getIssuer().toString().replace("\"", ""));
					objC.put("Signature Algorithm", cd.getSignatureAlgorithm());
					objC.put("Serial Number", cd.getSerialNumber());
					objC.put("Serial Number(HEX)", cd.getSerialNumber1());
					objC.put("Alias(Label)", "N/A");
					objC.put("Keystore", "N/A");
					objC.put("Expires On", GetLocalTime.run(expTimeL));
					objC.put("Issued On", GetLocalTime.run(bornTimeL));
					objC.put("Status", status);
					objC.put("Private(Personal) Key", pKey);

					//JSONArray list = new JSONArray();
					objC.put("SAN Entries", clist);
					objC.put("Chained Certificates", "N/A");

					logger.debug(" [GenerateJSONSimple] final string completed for "+certificate);
					finalString=finalString+"\n"+objC.toJSONString().replace("\\","");

				} catch (CertificateParsingException cpe) {
					logger.debug(cpe+" "+certificate);
				} catch (CertificateException ce) {
					logger.debug(ce+" "+certificate);
				} catch (FileNotFoundException fnfe) {
					logger.debug(fnfe+" "+certificate);
				}

			}

		}
		//System.out.println(finalString);
		JSONFileWriter.run(outFile, finalString);

		logger.info("Certificate Report Generator completed the JSON report.");
		logger.info("Please check the file: "+outFile);
		logger.debug(" [GenerateJSONSimple] end");

	}

}