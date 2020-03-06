package com.certificate.tracker.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

public class FindKeystores {

	private final static Logger logger = Logger.getLogger(FindKeystores.class);
	private static List<String> result = new ArrayList<String>();
	
	public static List<String> run(File directory, String extension) {		
		if (directory.isDirectory()) {
			search(directory, extension);
		} else {
			logger.error(directory.getAbsoluteFile() + " is NOT a valid directory/ path !");
			logger.debug("will exit with -2");
			logger.error("Certificate-Tracker App exited with error.\n");
			System.exit(-2);
		}		
		return result;
	}

	private static void search(File file, String extension) {

		if (file.isDirectory()) {
			logger.info("Searching directory : [" + file.getAbsoluteFile() + "]");
			if (file.canRead()) {
				for (File filetmp : file.listFiles()) {
					if (filetmp.isDirectory()) {
						search(filetmp, extension);
					} else {
						String fileExtension = FilenameUtils.getExtension(filetmp.toString());
						fileExtension = fileExtension.toLowerCase();
						logger.debug("fileExtension("+ extension +") :"+fileExtension);
						if (fileExtension.equals(extension)) {
							result.add(filetmp.getAbsoluteFile().toString());
							logger.debug(result);
						}
					}
				}
			} else {
				logger.error(file.getAbsoluteFile() + " access denied.");
			}
		}

	}

}