/*
version 1.0
date 08/25/2016.
program name CertificateDetails.java
*/

// Imports.
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;


// Class declaration.
public class CertificateDetails {
        // Main method.
        public static void main(String[] args) throws Exception {
        // Provide the certificate name as the input.
        FileInputStream fileInputStream = new FileInputStream(args[0]);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        System.out.println(x509Certificate.getSubjectDN());
        System.out.println(x509Certificate.getIssuerDN());
        System.out.println(x509Certificate.getNotBefore());
        System.out.println(x509Certificate.getNotAfter());
        System.out.println(x509Certificate.getSerialNumber().toString(16));
        System.out.println(x509Certificate.getSigAlgName());
        // Subjective Alternative Names.
        for (List<?> SAN: x509Certificate.getSubjectAlternativeNames()) {
            System.out.println(SAN.get(1));
        }
  }
}

// End.
