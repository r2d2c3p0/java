package certificate;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

public class CertificateDetails {
	public static void main(String string, String verboseflag) throws Exception, IOException {
		String CertificateName = null;
		try {
             CertificateName = string;
        } catch (ArrayIndexOutOfBoundsException exception) {
             System.out.println("  null input");
             System.exit(1);
        }
        FileInputStream fileInputStream = new FileInputStream(CertificateName);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
		X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        String cName = x509Certificate.getSubjectDN().toString();
	    String Exp2 = x509Certificate.getNotAfter().toString();
        BigInteger sNumber = x509Certificate.getSerialNumber();
        String Sig = x509Certificate.getSigAlgName();
        String SANEntries = "";
        if (verboseflag == "--verbose") {
			System.out.println("  "+x509Certificate.getSubjectDN());
	        System.out.println("  "+x509Certificate.getIssuerDN());
	        System.out.println("  "+x509Certificate.getNotBefore());
	        System.out.println("  "+x509Certificate.getNotAfter());
	        System.out.println("  "+x509Certificate.getSerialNumber().toString());
	        System.out.println("  "+x509Certificate.getSigAlgName());
	    }
        // Subjective Alternative Names.
        for (List<?> SAN: x509Certificate.getSubjectAlternativeNames()) {
        	if (verboseflag == "--verbose") {
        		System.out.println("\t"+SAN.get(1));
        	}
        	SANEntries = SANEntries+"<li>"+SAN.get(1)+"</li>";
        }
        GenerateHTML.htmlTable(cName, sNumber, Exp2, SANEntries, Sig);
	}
}