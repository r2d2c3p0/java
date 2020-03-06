package org.r2d2c3p0.cr.generator.html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class HTMLFileAppender {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	public static void run(String filename, String data) {

		logger.debug(" [HTMLFileAppender] start.");
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		File file = new File(filename);

		try {
			fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(data);
			logger.debug(" [HTMLFileAppender] finished writing data.");
		} catch (IOException ioe) {
			logger.error(" [HTMLFileAppender] "+ioe+" "+filename);
		} finally {
			try {
				if (bufferedWriter != null) bufferedWriter.close();
				if (fileWriter != null) fileWriter.close();
			} catch (IOException ioe) {
				logger.error(" [HTMLFileAppender] "+ioe+" "+filename);
			}
		}
		logger.debug(" [HTMLFileAppender] end.");
	}

}