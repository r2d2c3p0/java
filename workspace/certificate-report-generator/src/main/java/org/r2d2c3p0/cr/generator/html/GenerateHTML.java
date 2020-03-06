package org.r2d2c3p0.cr.generator.html;

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
import java.util.ListIterator;

import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;
import org.r2d2c3p0.cr.generator.csv.CertificateDetails;
import org.r2d2c3p0.utils.CMSKeystore;
import org.r2d2c3p0.utils.ChecksAndValidations;
import org.r2d2c3p0.utils.GetLocalTime;
import org.r2d2c3p0.utils.KeystorePassword;

public class GenerateHTML {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");
	private static final long MM_SECONDS_PER_DAY = 1000 * 60 * 60 * 24;

	private static final Date curDate = new Date();
	private static final long curTime = curDate.getTime();

	public static void run(List<String> fileFinalList, int i, String encFile, String outFile, String fileIdentifier) {

		logger.debug(" [GenerateHTML] start.");
		HTMLConfig htmlConfig = ConfigFactory.create(HTMLConfig.class);
		if (htmlConfig.checkInternet().equals("enable")) {
			if (CheckInternetConnection.run()) {
				HTMLFileWriter.run(
						outFile,
						MainPage.print(
								htmlConfig.faCSS(),
								htmlConfig.jqDTCSS(),
								htmlConfig.jqJS(),
								htmlConfig.jqDTJS(),
								htmlConfig.dtJS(),
								fileIdentifier
								)
						);
			} else {
					HTMLFileWriter.run(
							outFile,
							MainPage.print(
									"\"configuration/css/font-awesome.min.css\"",
									"\"configuration/css/jquery.dataTables.css\"",
									"\"configuration/js/jquery-3.1.0.js\"",
									"\"configuration/js/jquery.dataTables.js\"",
									"\"configuration/js/dataTables.select.min.js\"",
									fileIdentifier
									)
							);
			}
		} else {
			HTMLFileWriter.run(
					outFile,
					MainPage.print(
							htmlConfig.faCSS(),
							htmlConfig.jqDTCSS(),
							htmlConfig.jqJS(),
							htmlConfig.jqDTJS(),
							htmlConfig.dtJS(),
							fileIdentifier
							)
					);
		}
		HTMLFileWriter.run(
				htmlConfig.jsFile().replace("main.js", fileIdentifier+"_main.js"),
				MainJSPage.print()
				);

		if (i==0) {
			for (String keystore : fileFinalList) {
				KeyStore keystoreInstance;
				try {
					keystoreInstance = ChecksAndValidations.PreChecksAndValidations(keystore);
					logger.debug(" [GenerateHTML] Keystore instance :"+keystoreInstance);
					try {
						logger.info("Working on "+keystore+" ... ");
						keystoreInstance.load(new FileInputStream(keystore),
								KeystorePassword.run(keystore, keystoreInstance, encFile)
								.toCharArray());

						Enumeration<String> aliasEnumumeration = keystoreInstance.aliases();
						while (aliasEnumumeration.hasMoreElements()) {
							String cAlias = aliasEnumumeration.nextElement();
							logger.info("\t\tWorking on "+cAlias+" ... ");
							Certificate[] chainCertificates;
							List<String> chainList = new ArrayList<String>();
							X509Certificate x509certificate = (X509Certificate) keystoreInstance
									.getCertificate(cAlias);

							if (keystoreInstance.isKeyEntry(cAlias)) {
								chainCertificates = keystoreInstance.getCertificateChain(cAlias);

								for (int ce=1; ce<chainCertificates.length; ce++) {
									X509Certificate certchain = (X509Certificate) chainCertificates[ce];
									chainList.add(certchain.getSubjectDN().toString());
								}

							}

							Date expTime = x509certificate.getNotAfter();
							long expTimeL = expTime.getTime();
							Date bornTime = x509certificate.getNotBefore();
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
							String pKey = "Trust Entry";
							if (CMSKeystore.PrivateKey(keystore, cAlias, encFile)) pKey = "Key Entry";

							Collection<List<?>> san;
							List<String> clist = new ArrayList<String>();
							try {
								san = x509certificate.getSubjectAlternativeNames();
								for (List<?> SAN: san) {
									clist.add((String) SAN.get(1));
								}
							} catch (NullPointerException npe) {
								san=null;
							}

							/*
							 * Remove "\"" in CA CN String
							*/
							for (int j = 0; j < chainList .size(); j++) {
							    String value = chainList.get(j);
							    if (value.contains("\"")) {
							      value = value.replace("\"", "");
							      chainList.set(j, value);
							    }
							}

							HTMLFileAppender.run(htmlConfig.jsFile().replace("main.js", fileIdentifier+"_main.js"),
									"    {\n"
											+"      \"commonname\": \""+x509certificate.getSubjectDN().toString().replace("\"", "")+"\",\n"
											+"      \"alias\": \""+cAlias.toString().replace("\"", "")+"\",\n"
											+"      \"serialnumber\": \""+x509certificate.getSerialNumber().toString()+"\",\n"
											+"      \"expires\": \""+GetLocalTime.run(expTimeL)+"\",\n"
											+"      \"issued\": \""+GetLocalTime.run(bornTimeL)+"\",\n"
											+"      \"pkey\": \""+pKey+"\",\n"
											+"      \"sstatus\": \""+status+"\",\n"
											+"      \"keystore\": \""+keystore.toString().replace("\\", "\\\\")+"\",\n"
											+"	  \"chain\": \""+chainList+"\",\n"
											+"	  \"issuer\": \""+x509certificate.getIssuerDN().toString().replace("\"", "")+"\",\n"
											+"	  \"san\": \""+clist+"\",\n"
											+"	  	\"sigal\": \""+x509certificate.getSigAlgName()+"\",\n"
											+"      \"aserialnumber\": \""+x509certificate.getSerialNumber().toString(16)+"\"\n"
											+"    },\n"
									);
						}
					} catch (NoSuchAlgorithmException nsae) {
						logger.debug(" [GenerateHTML] "+nsae);
					} catch (CertificateException ce) {
						logger.debug(" [GenerateHTML] "+ce);
					} catch (FileNotFoundException fnfe) {
						logger.debug(" [GenerateHTML] "+fnfe);
					} catch (IOException ioe) {
						logger.debug(" [GenerateHTML] "+ioe);
					} catch (NullPointerException npe) {
						logger.debug(" [GenerateHTML] "+npe);
					}

				} catch (KeyStoreException kse) {
					logger.error("[GenerateHTML] KeyStoreException occured");
					logger.debug(" [GenerateHTML] "+kse);
				}
			}
		}

		if (i==1) {

			for (String certificate : fileFinalList) {
				logger.info("Working on "+certificate+" ... ");
				try {
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
					HTMLFileAppender.run(htmlConfig.jsFile().replace("main.js", fileIdentifier+"_main.js"),
							"    {\n"
									+"      \"commonname\": \""+cd.getCommonName().toString().replace("\"", "")+"\",\n"
									+"      \"alias\": \"N/A\",\n"
									+"      \"serialnumber\": \""+cd.getSerialNumber()+"\",\n"
									+"      \"expires\": \""+GetLocalTime.run(expTimeL)+"\",\n"
									+"      \"issued\": \""+GetLocalTime.run(bornTimeL)+"\",\n"
									+"      \"pkey\": \""+pKey+"\",\n"
									+"      \"sstatus\": \""+status+"\",\n"
									+"      \"keystore\": \"N/A\",\n"
									+"	  \"chain\": \"N/A\",\n"
									+"	  \"issuer\": \""+cd.getIssuer().toString().replace("\"", "")+"\",\n"
									+"	  \"san\": \""+clist+"\",\n"
									+"	  	\"sigal\": \""+cd.getSignatureAlgorithm()+"\",\n"
									+"      \"aserialnumber\": \""+cd.getSerialNumber1()+"\"\n"
									+"    },\n"
							);
				} catch (CertificateParsingException cpe) {
					logger.debug(" [GenerateHTML] "+cpe+" "+certificate);
				} catch (CertificateException ce) {
					logger.debug(" [GenerateHTML] "+ce+" "+certificate);
				} catch (FileNotFoundException fnfe) {
					logger.debug(" [GenerateHTML] "+fnfe+" "+certificate);
				}

			}
		}

		HTMLFileAppender.run(htmlConfig.jsFile().replace("main.js", fileIdentifier+"_main.js"),
				"    {\n"
						+"      \"commonname\": \"Certificate Common Name.\",\n"
						+"      \"sstatus\": \"Valid or Expired.\",\n"
						+"      \"serialnumber\": \"Serial Number in Decimal.\",\n"
						+"      \"expires\": \"Expiration Date.\",\n"
						+"      \"issued\": \"Issuance Date.\",\n"
						+"      \"pkey\": \"Indicates Private Key.\",\n"
						+"      \"alias\": \"Certificate Alias.\",\n"
						+"      \"keystore\": \"Keystore Name(where this certificate is stored)\",\n"
						+"	  	\"chain\": \"Private Key Chained Certificates.\",\n"
						+"	  	\"issuer\": \"Issued By, CA Name.\",\n"
						+"	  	\"san\": \"\",\n"
						+"	  	\"sigal\": \"\",\n"
						+"      \"aserialnumber\": \"Serial Number in Hexadecimal.\"\n"
						+"    }\n"
				);
		HTMLFileAppender.run(htmlConfig.jsFile().replace("main.js", fileIdentifier+"_main.js"),
				"  ]\n}\n"
				);

		logger.info("Certificate Report Generator completed the HTML report.");
		logger.info("Please check the file: "+outFile);
		logger.debug(" [GenerateHTML] end.");

	}

	public static ListIterator<String> replace(List<String> list) {
	    ListIterator<String> it = list.listIterator();
	    while(it.hasNext()) {
	        it.set(it.next().replace("\"",""));
	    }
	    return it;
	}

}