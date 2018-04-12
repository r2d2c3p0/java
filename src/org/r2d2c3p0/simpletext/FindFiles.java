package org.r2d2c3p0.simpletext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

public class FindFiles {

	private static List<String> result = new ArrayList<String>();

	public static List<String> run(File directory, String extension) {
		if (directory.isDirectory()) {
			search(directory, extension);
		} else {
			System.exit(-2);
		}
		return result;
	}

	private static void search(File file, String extension) {

		if (file.isDirectory()) {
			try {
				if (file.canRead()) {
					for (File filetmp : file.listFiles()) {
						if (filetmp.isDirectory()) {
							search(filetmp, extension);
						} else {
							String fileExtension = FilenameUtils.getExtension(filetmp.toString());
							fileExtension = fileExtension.toLowerCase();
							if (fileExtension.equals(extension)) {
								result.add(filetmp.getAbsoluteFile().toString());
							}
						}
					}
				}
			} catch (NullPointerException npe) {
			}
		}

	}

}