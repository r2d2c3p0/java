package custom.ssl.tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

public class CertificateDetails {

	public static BigInteger CertificateSerialNumber(String CertificateFileName) throws 
		FileNotFoundException, 
		CertificateException {

		FileInputStream fileInputStream = new FileInputStream(CertificateFileName);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        return x509Certificate.getSerialNumber();

	}
	
	public static void CertificateAllDetails(String CertificateFileName) throws 
		FileNotFoundException, 
		CertificateException {
		
		FileInputStream fileInputStream = new FileInputStream(CertificateFileName);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        System.out.println();
        System.out.println("\tSubject CN:  "+x509Certificate.getSubjectDN());
        System.out.println("\tIssuer CN:  "+x509Certificate.getIssuerDN());
        System.out.println("\tIssuation Date:  "+x509Certificate.getNotBefore());
        System.out.println("\tExpiration Date:  "+x509Certificate.getNotAfter());
        System.out.println("\tSerial Number(HEX):  "+x509Certificate.getSerialNumber().toString(16));
        System.out.println("\tSerial Number:  "+x509Certificate.getSerialNumber().toString());
        System.out.println("\tSigning Algorithm:  "+x509Certificate.getSigAlgName());
        
        // Subjective Alternative Names.
        try {
			for (List<?> SAN: x509Certificate.getSubjectAlternativeNames()) {
				System.out.println("\t\tSAN entry:  "+SAN.get(1));
			}
		} catch (Exception e1) {
			System.out.println("\t\tSAN entries:  none.");
		}
        System.out.println();
        System.exit(1);
	}

}