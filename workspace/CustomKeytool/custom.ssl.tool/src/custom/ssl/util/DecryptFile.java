package custom.ssl.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class DecryptFile {

	public static void decrypt(String data) {

		String FILENAME = "decrypted.file";
		BufferedWriter bufferedwriter = null;FileWriter filewriter = null;
		
		try {
			File file = new File(FILENAME);
			if (!file.exists()) { file.createNewFile(); }
			filewriter = new FileWriter(file.getAbsoluteFile(), true);
			bufferedwriter = new BufferedWriter(filewriter);
			String dec;
			try {
				dec = BCEncrypterDecrypter.decrypt(data, "xxxx");
				bufferedwriter.write(dec+"\n");
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bufferedwriter != null) { bufferedwriter.close(); }
				if (filewriter != null) { filewriter.close(); }
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		BufferedReader bufferedreader = null;FileReader filereader = null;
		
        try {
        	filereader = new FileReader(args[0]);
        	bufferedreader = new BufferedReader(filereader);
        	String current_line;
        	while ((current_line = bufferedreader.readLine()) != null) {
        		try {
        			//decrypt(sCurrentLine);
        			System.out.println("\t"+BCEncrypterDecrypter.decrypt(current_line, "xxxx"));
        		} catch (Exception exception) {
        			exception.printStackTrace();
        		}
        	}
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        } finally {
        	try {
        		if (bufferedreader != null) { bufferedreader.close(); if (filereader != null) { filereader.close(); } }
        	} catch (IOException ioexception) {
        		ioexception.printStackTrace();
        	}
        }
	}

}
