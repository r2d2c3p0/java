package custom.ssl.tool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertificateDetails {

	public static BigInteger CertificateDetailsMethod(String CertificateFileName) throws FileNotFoundException, CertificateException {

		FileInputStream fileInputStream = new FileInputStream(CertificateFileName);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        return x509Certificate.getSerialNumber();

	}

}