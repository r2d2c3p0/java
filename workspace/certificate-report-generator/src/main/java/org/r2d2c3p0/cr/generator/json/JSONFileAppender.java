package org.r2d2c3p0.cr.generator.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class JSONFileAppender {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	public static void run(String filename, String data) {

		logger.debug(" [JSONFileAppender] start.");
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		File file = new File(filename);

		try {
			fileWriter = new FileWriter(file.getAbsoluteFile(), true);
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(data);
			logger.debug(" [JSONFileAppender] finished writing data.");
		} catch (IOException ioe) {
			logger.error(" [JSONFileAppender] "+ioe+" "+filename);
		} finally {
			try {
				if (bufferedWriter != null) bufferedWriter.close();
				if (fileWriter != null) fileWriter.close();
			} catch (IOException ioe) {
				logger.error(" [JSONFileAppender] "+ioe+" "+filename);
			}
		}
		logger.debug(" [JSONFileAppender] end.");
	}

}