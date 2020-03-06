package org.r2d2c3p0.cr.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.r2d2c3p0.cr.generator.csv.GenerateCSV;
import org.r2d2c3p0.cr.generator.html.GenerateHTML;
import org.r2d2c3p0.cr.generator.json.GenerateJSONSimple;
import org.r2d2c3p0.utils.CRGeneratorConfig;
import org.r2d2c3p0.utils.FindFiles;
import org.r2d2c3p0.utils.GetVMArguments;

import com.fasterxml.jackson.core.JsonGenerationException;

public class App {

	/**
	 * @certificateFinalList final certificate list input to the application.
	 * @keystoreFinalList final key store list input to the application.
	*/
	private static List<String> keystoreFinalList = new ArrayList<String>();
	private static List<String> certificateFinalList = new ArrayList<String>();

	private static String empty = new String();

	/*
	 * Logger class initializer.
	*/
	private final static Logger logger = Logger.getLogger("CRGAppLogger");
	private final static Logger fflogger = Logger.getLogger("FindFilesLogger");
	/*
	 * Log and Console pretty print place holders.
	 * loggerBegin
	 * loggerNonZeroExit
	*/
	static void loggerBegin() {
		logger.info("Starting Certificate Report Generator App.");
		GetVMArguments.run();
	}

	static void loggerNonZeroExit(String message) {
		logger.info(" properties file :"+message);
    	logger.error("Certificate Report Generator App exited with error.\n");
    	System.exit(-9);
	}

    public static void main(String[] arguments) {

    	CRGeneratorConfig crgConfig;
    	String fileIdentifier="CRG";

    	if (arguments.length > 0) {
        	fileIdentifier=arguments[0];
        	if (arguments.length > 1) {
	            Properties applicationProperties = new Properties();
	            try {
	            	applicationProperties.load(new FileInputStream(new File(arguments[1])));
				} catch (FileNotFoundException fnfe) {
					System.out.println(" [App] loading properties file "+fnfe);
					System.exit(-1);
				} catch (IOException ioe) {
					System.out.println(" [App] loading properties file "+ioe);
					System.exit(-1);
				}
	            crgConfig = ConfigFactory.create(CRGeneratorConfig.class, applicationProperties);
        	} else {
        		crgConfig = ConfigFactory.create(CRGeneratorConfig.class);
        	}
        } else {
        	crgConfig = ConfigFactory.create(CRGeneratorConfig.class);
        }

        PropertyConfigurator.configure(crgConfig.l4jFile());
        logger.debug(" [App] log4j initialized.");
        loggerBegin();

        /**
		 *
		 * @gotCSV - place holder for CSV output file generation.
		 * @gotJSON - place holder for JSON output file generation.
		 * @gotHTML - place holder for HTML output file generation.
		 * @gotCer - place holder for Certificates only output.
		 * @gotHTML - place holder for Key stores only output.
		 * default is false.
		 *
		**/
		boolean gotHTML= false;boolean gotCSV= false;boolean gotJSON= false;
		boolean gotCer= false;boolean gotKeys= false;

		if (crgConfig.outputFormat().toUpperCase().equals("CSV")) {
			gotCSV=true;logger.debug(" [App] gotCSV is true.");
		} else if (crgConfig.outputFormat().toUpperCase().equals("JSON")) {
			gotJSON=true;logger.debug(" [App] gotJSON is true.");
		} else if (crgConfig.outputFormat().toUpperCase().equals("HTML")) {
			gotHTML=true;logger.debug(" [App] gotHTML is true.");
		} else {
			logger.error("[App] Output format is not permitted.");
			loggerNonZeroExit(" [App] check report.output.format");
		}

		if (crgConfig.rootPath().equals(empty)) {
			logger.error("[App] Starting (Root) path is not provided.");
			loggerNonZeroExit(" [App] check root.full.path");
		}

		if (!crgConfig.cExtensions().equals(empty)) {
			gotCer=true;
			logger.debug(" [App] gotCer is true.");
		}

		if (!crgConfig.kExtensions().equals(empty)) {
			gotKeys=true;
			logger.debug(" [App] gotKeys is true.");
		}

		if (gotKeys && gotCer) {
			logger.error("[App] Multiple types are provided.");
			logger.info(crgConfig.cExtensions()+" and "+crgConfig.kExtensions());
			loggerNonZeroExit(" [App] check keystore.file.extentions "
					+ "and certificate.file.extentions");
		}

		if (!gotKeys && !gotCer) {
			logger.error("[App] File extensions are not provided.");
			loggerNonZeroExit(" [App] check keystore.file.extentions "
					+ "and certificate.file.extentions");
		}

		if (gotKeys) {
			logger.info("[App] keystore extensions :"+crgConfig.kExtensions());
			File rootFile = new File(crgConfig.rootPath());

			/*
			 * split the string using ','
			 * allows for multiple options.
			*/
			final String[] kExts = crgConfig.kExtensions().split(Pattern.quote(","));

			for (int iKS=0; iKS<kExts.length; iKS++) {
				logger.debug(" [App] keystore extension selected: " + kExts[iKS]);
				/*
				 * trims whitespace before calling the class.
				*/
				keystoreFinalList = FindFiles
						.run(rootFile, kExts[iKS].trim().toLowerCase());
			}

			logger.info(keystoreFinalList);
			logger.info("[App] Total keystores found: "+keystoreFinalList.size());
		}

		if (gotCer) {
			logger.info("[App] Certificate extensions :"+crgConfig.cExtensions());
			File rootFile = new File(crgConfig.rootPath());

			/*
			 * split the string using ','
			 * allows for multiple options.
			*/
			final String[] cExts = crgConfig.cExtensions().split(Pattern.quote(","));

			for (int iC=0; iC<cExts.length; iC++) {
				logger.debug(" [App] certificate extension selected: " + cExts[iC]);
				/*
				 * trims whitespace before calling the class.
				*/
				certificateFinalList = FindFiles
						.run(rootFile, cExts[iC].trim().toLowerCase());
			}

			fflogger.info(certificateFinalList);
			logger.info("Total certificates found: "+certificateFinalList.size());
		}

		if (gotHTML) {
			/*
			 *  GenerateHTML - class to generate the report in HTML format.
			 *  Parses the key stores and prints the following information for each entry.
			 *  Common Name.
			 *  Serial Number (in Decimal and Hexadecimal)
			 *  Alias.
			 *  Issuing Authority.
			 *  Validity Period.
			 *	SAN Entries
			 *  Chain Certificates.
			 *  Signature Algorithm (SHA1 or SHA256)
			*/
			if (gotCer) {
				GenerateHTML.run(certificateFinalList,
						1,
						crgConfig.encryptedFile(),
						fileIdentifier+crgConfig.htmlFile(),
						fileIdentifier);
			} else {
				GenerateHTML.run(keystoreFinalList,
						0,
						crgConfig.encryptedFile(),
						fileIdentifier+crgConfig.htmlFile(),
						fileIdentifier);
			}
		}

		if (gotCSV) {
			/*
			 *  CRGeneratorCSV - class to generate the report in CSV format.
			 *  Parses the key stores and prints the following information for each entry.
			 *  Common Name.
			 *  Serial Number (in Decimal and Hexadecimal)
			 *  Alias.
			 *  Issuing Authority.
			 *  Validity Period.
			 *
			 *  TO-DO - match JSON format entries.
			*/
			if (gotCer) {
				try {
					GenerateCSV.run(certificateFinalList,
							1,
							crgConfig.encryptedFile(),
							fileIdentifier+crgConfig.csvFile());
				} catch (NoSuchMethodError nsme) {
					logger.error("Check JAVA version, need 8 or more.");
					logger.error(nsme);
					loggerNonZeroExit(" [App] GenerateCSV.run failed.");
				}
			} else {
				try {
					GenerateCSV.run(keystoreFinalList,
							0,
							crgConfig.encryptedFile(),
							fileIdentifier+crgConfig.csvFile());
				} catch (NoSuchMethodError nsme) {
					logger.error("Check JAVA version, need 8 or more.");
					logger.error(nsme);
					loggerNonZeroExit(" [App] GenerateCSV.run failed.");
				}
			}
		}

		if (gotJSON) {
			/*
			 *  GenerateJSONSimple - class to generate the report in JSON format.
			 *  Parses the key stores and prints the following information for each entry.
			 *  Common Name.
			 *  Serial Number (in Decimal and Hexadecimal)
			 *  Private Key.
			 *  Alias.
			 *  Issuing Authority.
			 *  Validity Period.
			 *  Signature Algorithm (SHA1 or SHA256)
			 *  SAN Entries.
			 *  Chained Certificates.
			*/
			if (gotCer) {
				try {
					GenerateJSONSimple.run(certificateFinalList,
							1,
							crgConfig.encryptedFile(),
							fileIdentifier+crgConfig.jsonFile());
				} catch (JsonGenerationException jge) {
					logger.error(" [App] certificate "+jge);
					loggerNonZeroExit(" [App] GenerateJSONSimple.run failed.");
				}
			} else {
				try {
					GenerateJSONSimple.run(keystoreFinalList,
							0, crgConfig.encryptedFile(),
							fileIdentifier+crgConfig.jsonFile());
				} catch (JsonGenerationException jge) {
					logger.error(" [App] keystore "+jge);
					loggerNonZeroExit(" [App] GenerateJSONSimple.run failed.");
				}
			}
		}

		logger.info("End Certificate Report Generator App.\n");
		System.exit(0);

    }

}