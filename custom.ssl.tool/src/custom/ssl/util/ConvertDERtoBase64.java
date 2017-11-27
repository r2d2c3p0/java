package custom.ssl.util;

import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class ConvertDERtoBase64 {
	
	public static String ConvertToPEM(X509Certificate certificate) {
		
		Base64 encoder = new Base64();
		byte[] DERCertificate;
		
		String certificate_begin = "-----BEGIN CERTIFICATE-----";
		String certificate_end = "-----END CERTIFICATE-----";
		
		try {
			DERCertificate = certificate.getEncoded();
			String PEMCertificatePre = new String(encoder.encode(DERCertificate));
			String PEMCertificate = certificate_begin + "\n" + PEMCertificatePre + "\n" + certificate_end;
			return PEMCertificate;
		} catch (CertificateEncodingException cee) {
			//cee.printStackTrace();
			System.out.println("\tConvertDERtoBase64 CertificateException, not a valid certificate.");
			
		}		
		return null;		

	}
	
	public static void ConvertDERtoBase64Main(String certificateFileName, int tFlag) throws CertificateEncodingException {
		
		try {
			FileInputStream fileInputStream = new FileInputStream(certificateFileName);
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
	        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
	        if (tFlag ==0) {
	        	System.out.println(ConvertToPEM(x509Certificate));
	        } else {
	        	String FILENAME = certificateFileName+"_base64.cer";
	    		BufferedWriter bufferedwriter = null;
	    		FileWriter filewriter = null;
	    		try {
	    			File file = new File(FILENAME);
	    			if (!file.exists()) { file.createNewFile(); }
	    			filewriter = new FileWriter(file.getAbsoluteFile(), true);
	    			bufferedwriter = new BufferedWriter(filewriter);
	    			try {
	    				bufferedwriter.write(ConvertToPEM(x509Certificate));
	    			} catch (Exception exp) {
	    				exp.printStackTrace();
	    			}
	    		} catch (IOException ioe) {
	    			ioe.printStackTrace();
	    		} finally {
	    			try {
	    				if (bufferedwriter != null) { bufferedwriter.close(); }
	    				if (filewriter != null) { filewriter.close(); }
	    			} catch (IOException ioex) {
	    				ioex.printStackTrace();
	    			}
	    		}
	        }
	        
		} catch (FileNotFoundException fnfe) {
			//fnfe.printStackTrace();
			System.out.println("\tConvertDERtoBase64 FileNotFoundException, "+certificateFileName+" not found.");
		} catch (CertificateException cee) {
			//cee.printStackTrace();
			System.out.println("\tConvertDERtoBase64.main CertificateException, not a valid certificate.");
		}
        
	}
}