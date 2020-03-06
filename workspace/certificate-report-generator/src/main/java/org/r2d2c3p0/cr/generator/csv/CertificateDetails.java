package org.r2d2c3p0.cr.generator.csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.Principal;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class CertificateDetails {

	String SerialNumber1;
	String SerialNumber;
	Principal Issuer;
	Date Expiration;
	Date Issued;
	Principal CommonName;
	String SignatureAlgorithm;
	Collection<List<?>> SAN;

	public static BigInteger CertificateSerialNumber(String CertificateFileName) throws	FileNotFoundException, CertificateException {

		FileInputStream fileInputStream = new FileInputStream(CertificateFileName);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        return x509Certificate.getSerialNumber();

	}

	public CertificateDetails(String CertificateFileName) throws FileNotFoundException, CertificateException {

		FileInputStream fileInputStream = new FileInputStream(CertificateFileName);
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
        X509Certificate x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
        this.CommonName=x509Certificate.getSubjectDN();
        this.Issuer=x509Certificate.getIssuerDN();
        this.Expiration=x509Certificate.getNotAfter();
        this.Issued=x509Certificate.getNotBefore();
        this.SerialNumber=x509Certificate.getSerialNumber().toString();
        this.SerialNumber1=x509Certificate.getSerialNumber().toString(16);
        this.SignatureAlgorithm=x509Certificate.getSigAlgName();
        this.SAN=x509Certificate.getSubjectAlternativeNames();

	}

	public String getSerialNumber() {
		return SerialNumber;
	}

	public String getSerialNumber1() {
		return SerialNumber1;
	}

	public Principal getIssuer() {
		return Issuer;
	}

	public Date getExpiration() {
		return Expiration;
	}

	public Date getIssued() {
		return Issued;
	}

	public Principal getCommonName() {
		return CommonName;
	}

	public String getSignatureAlgorithm() {
		return SignatureAlgorithm;
	}

	public Collection<List<?>> getSAN() {
		return SAN;
	}
}