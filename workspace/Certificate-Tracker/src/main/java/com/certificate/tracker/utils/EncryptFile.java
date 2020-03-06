package com.certificate.tracker.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class EncryptFile {
	
	private final static Logger logger = Logger.getLogger(EncryptFile.class);
	
	public static void encrypt(String data) {

		logger.debug(" start");
		String FILENAME = "encrypted.file";
		BufferedWriter bufferedwriter = null;
		FileWriter filewriter = null;
		  
		try {
			File file = new File(FILENAME);
			if (!file.exists()) { file.createNewFile(); }
			filewriter = new FileWriter(file.getAbsoluteFile(), true);
			bufferedwriter = new BufferedWriter(filewriter);
			String encrypted_string;
			try {
				encrypted_string = BCEncrypterDecrypter.encrypt(data, 
						"e0l1p2m3i4s5r6e7v8e9n11d12n13"
						+ "a14e15r16u17p18y19"
						+ "l20e21r22a23r24s25"
						+ "i26h27t28u29r30t31"
						+ "e32h33t34");
				bufferedwriter.write(encrypted_string+"\n");
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bufferedwriter != null) { bufferedwriter.close(); }
				if (filewriter != null) { filewriter.close(); }
			} catch (IOException ioexception) {
				ioexception.printStackTrace();
			}
		}
		logger.debug(" end");
	}

	public static void main(String[] args) {

		logger.debug(" start");
		BufferedReader bufferedreader = null;
		FileReader filereader = null;
        
        try {
        	filereader = new FileReader(args[0]);
        	bufferedreader = new BufferedReader(filereader);
        	String sCurrentLine;
        	while ((sCurrentLine = bufferedreader.readLine()) != null) {
        		try {
        			encrypt(sCurrentLine);
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        } catch (IOException ioe) {
        	ioe.printStackTrace();
        } finally {
        	try {
        		if (bufferedreader != null) { bufferedreader.close(); 
        		if (filereader != null) { filereader.close(); } }
        	} catch (IOException ioexception) {
        		ioexception.printStackTrace();
        	}
        }
        
        logger.debug(" end");
        
	}

}