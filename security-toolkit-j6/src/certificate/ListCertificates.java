package certificate;

import java.io.File;

public class ListCertificates {
	private static String VerboseFlag = null;
	public static void main(String[] args) {
		try {
              VerboseFlag = args[0];
        } catch (ArrayIndexOutOfBoundsException exception) {
              // VerboseFlag is not returned.
        }
		File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        GenerateHTML.startHTML();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
               //System.out.println("File " + listOfFiles[i].getName());
            	try {
					CertificateDetails.main(listOfFiles[i].getName(), VerboseFlag);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
            } else if (listOfFiles[i].isDirectory()) {
               //System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
        GenerateHTML.endHTML();
	}
}