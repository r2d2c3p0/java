package org.r2d2c3p0.cr.generator.csv;

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
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;
import org.r2d2c3p0.utils.ChecksAndValidations;
import org.r2d2c3p0.utils.GetLocalTime;
import org.r2d2c3p0.utils.KeystorePassword;

public class GenerateCSV {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	private static final long MM_SECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	private static final Date curDate = new Date();
	private static final long curTime = curDate.getTime();

	public static void run(List<String> fileFinalList, int i, String encryptedFile, String outFile) {

		logger.debug(" [GenerateCSV] start.");
		if (!outFile.isEmpty()) {
			try {
				logger.info("CSV output file "+outFile);
				new FileWriter(outFile).close();
			} catch (IOException ioe) {
				logger.debug(" [GenerateCSV] "+ioe);
				logger.debug(" [GenerateCSV] outFile reset failed.");
			}
		}

	    BufferedWriter writer;
		try {
			writer = Files.newBufferedWriter(Paths.get(outFile));
			if (i==0) {
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
	                    .withHeader(
	                    		"Keystore",
	                    		"Alias",
	                    		"Common Name",
	                    		"Expiration",
	                    		"Issued on",
	                    		"Serial Number(HEX)",
	                    		"Serial Number",
	                    		"Status",
	                    		"Issuer"));
				logger.info(csvPrinter);
				for (String keystore : fileFinalList) {
					KeyStore keystoreInstance;
					try {
						logger.info("Working on "+keystore+" ... ");
						keystoreInstance = ChecksAndValidations.PreChecksAndValidations(keystore);
						logger.debug(" [GenerateCSV] Keystore instance :"+keystoreInstance);
						try {
								keystoreInstance.load(new FileInputStream(keystore),
										KeystorePassword.run(keystore, keystoreInstance, encryptedFile)
										.toCharArray());
								Enumeration<String> aliasEnumumeration = keystoreInstance.aliases();
								while (aliasEnumumeration.hasMoreElements()) {
									String cAlias = aliasEnumumeration.nextElement();
									logger.info("\t\tWorking on "+cAlias+" ... ");

									X509Certificate x509certificate = (X509Certificate) keystoreInstance
											.getCertificate(cAlias);

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
									csvPrinter.printRecord(keystore, cAlias,
											x509certificate.getSubjectDN().toString(),
											GetLocalTime.run(expTimeL),
											GetLocalTime.run(bornTimeL),
											x509certificate.getSerialNumber().toString(16),
											x509certificate.getSerialNumber().toString(),
											status,
											x509certificate.getIssuerDN());
								}
						} catch (NoSuchAlgorithmException nsae) {
							logger.debug(" [GenerateCSV] "+nsae);
						} catch (CertificateException ce) {
							logger.debug(" [GenerateCSV] "+ce);
						} catch (FileNotFoundException fnfe) {
							logger.debug(" [GenerateCSV] "+fnfe);
						} catch (IOException ioe) {
							logger.debug(" [GenerateCSV] "+ioe);
						} catch (NullPointerException npe) {
							logger.debug(" [GenerateCSV] "+npe);
						}

					} catch (KeyStoreException kse) {
						logger.error(" [GenerateCSV] KeyStoreException occured");
						logger.debug(kse);
					}
				}
				logger.debug(" \" [GenerateCSV] csv printer flush completed.");
				csvPrinter.flush();
			}

			if (i==1) {
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
	                    .withHeader(
	                    		"Common Name",
	                    		"Expiration",
	                    		"Issued on",
	                    		"Serial Number",
	                    		"Status",
	                    		"Issuer"));
				logger.info(csvPrinter);
				for (String certificate : fileFinalList) {

					logger.info("Working on "+certificate+" ... ");

					try {
						CertificateDetails cd = new CertificateDetails(certificate);

						Date expTime = cd.Expiration;
						long expTimeL = expTime.getTime();

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
						csvPrinter.printRecord(cd.CommonName, cd.Expiration, cd.Issued, cd.SerialNumber, status, cd.Issuer);
					} catch (CertificateParsingException cpe) {
						logger.debug(" [GenerateCSV] "+cpe+" "+certificate);
					} catch (CertificateException ce) {
						logger.debug(" [GenerateCSV] "+ce+" "+certificate);
					}

				}
				logger.debug(" \" [GenerateCSV] csv printer flush completed.");
				csvPrinter.flush();
			}

		} catch (IOException ioe) {
			logger.debug(" [GenerateCSV] "+ioe);
		}
		logger.info("Certificate Report Generator completed the CSV report.");
		logger.info("Please check the file: "+outFile);
		logger.debug(" [GenerateCSV] end");

	}

}