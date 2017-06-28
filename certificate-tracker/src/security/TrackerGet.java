package security;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class TrackerGet extends SimpleFileVisitor<Path> {
	
    public TrackerGet(String path) throws Exception {
        Files.walkFileTree(Paths.get(path), this);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
    	if (file.getFileName().toString().endsWith("cer") || file.getFileName().toString().endsWith("CER") || 
    			file.getFileName().toString().endsWith("der") || file.getFileName().toString().endsWith("DER") ||
    			file.getFileName().toString().endsWith("crt") || file.getFileName().toString().endsWith("CRT"))	{
    			 FileInputStream fileInputStream = new FileInputStream(file.toString());
    			 CertificateFactory certificateFactory = null;
    			 try {
    				 certificateFactory = CertificateFactory.getInstance("X509");
    			 } catch (CertificateException e) {
    				 // TODO Auto-generated catch block
    				 //e.printStackTrace();
    			 }
    			 X509Certificate x509Certificate = null;
    			 try {
    				 x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
    			 } catch (CertificateException e) {
    				 // TODO Auto-generated catch block
    				 //e.printStackTrace();
    			 }
    			 String cName = x509Certificate.getSubjectDN().toString();
    			 Date Exp2 = x509Certificate.getNotAfter();
    			 BigInteger sNumber = x509Certificate.getSerialNumber();
    			 String Sig = x509Certificate.getSigAlgName();
    			 String IssuerDN = x509Certificate.getIssuerDN().toString();
    			 Date Exp1 = x509Certificate.getNotBefore();
    			 // Subjective Alternative Names.
    			 try {
    				 Collection<List<?>> SANEntries = x509Certificate.getSubjectAlternativeNames();
    				 //for (List<?> SAN: SANEntries) {
    				 //	 System.out.println("\t"+SAN.get(1));
    				 //}
    				 GenerateHTML.htmlTable(cName, IssuerDN, sNumber, Exp1, Exp2, SANEntries, Sig);
				 } catch (CertificateParsingException e1) {
					// TODO Auto-generated catch block
					GenerateHTML.htmlTable(cName, IssuerDN, sNumber, Exp1, Exp2, null, Sig);
				 } catch (NullPointerException Nully) {
					// TODO Auto-generated catch block
					GenerateHTML.htmlTable(cName, IssuerDN, sNumber, Exp1, Exp2, null, Sig);
				 }    			
    	}
        return FileVisitResult.CONTINUE;
    }

    /*@Override
    public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) throws IOException {
        System.out.println(directory);
        return FileVisitResult.CONTINUE;
    }*/

}