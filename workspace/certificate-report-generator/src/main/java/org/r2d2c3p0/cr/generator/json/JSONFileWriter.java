package org.r2d2c3p0.cr.generator.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class JSONFileWriter {

	private final static Logger logger = Logger.getLogger("CRGAppLogger");

	public static void run(String filename, String data) {

		logger.debug(" [JSONFileWriter] start.");
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter = null;
		File file = new File(filename);

		try {
			if (!file.exists()) {
				logger.info(" [JSONFileWriter] creating "+filename);
				file.createNewFile();
			}
			fileWriter = new FileWriter(file.getAbsoluteFile());
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(data);
			logger.debug(" [JSONFileWriter] finished writing data.");
		} catch (IOException ioe) {
			logger.error(" [JSONFileWriter] "+ioe+" "+filename);
		} finally {
			try {
				if (bufferedWriter != null) bufferedWriter.close();
				if (fileWriter != null) fileWriter.close();
			} catch (IOException ioe) {
				logger.error(" [JSONFileWriter] "+ioe+" "+filename);
			}
		}

		logger.debug(" [JSONFileWriter] end.");
	}

}