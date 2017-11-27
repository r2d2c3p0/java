package custom.ssl.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class EncryptFile {

	public static void encrypt(String data) {

		String FILENAME = "encrypted.file";
		BufferedWriter bufferedwriter = null;FileWriter filewriter = null;
		
		try {
			File file = new File(FILENAME);
			if (!file.exists()) { file.createNewFile(); }
			filewriter = new FileWriter(file.getAbsoluteFile(), true);
			bufferedwriter = new BufferedWriter(filewriter);
			String encrypted_string;
			try {
				encrypted_string = BCEncrypterDecrypter.encrypt(data, "e0l1p2m3i4s5r6e7v8e9n11d12n13a14e15r16u17p18y19l20e21r22a23r24s25i26h27t28u29r30t31e32h33t34");
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
	}

	public static void main(String[] args) {

		BufferedReader bufferedreader = null;FileReader filereader = null;
        
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
        		if (bufferedreader != null) { bufferedreader.close(); if (filereader != null) { filereader.close(); } }
        	} catch (IOException ioexception) {
        		ioexception.printStackTrace();
        	}
        }
	}

}