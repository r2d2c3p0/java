package custom.ssl.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Scanner;

import custom.ssl.util.ConvertDERtoBase64;

public class ExtractCertificates {

	@SuppressWarnings("resource")
	public static void ExtractCertificatesMain(String Keystore, String Password) throws
		KeyStoreException,
		NoSuchAlgorithmException,
		CertificateException {

		KeyStore ks = ChecksAndValidations.PreChecksAndValidations(Keystore);
		FileInputStream in1;

		try {
			in1 = new FileInputStream(Keystore);
			try {
				ks.load(in1, Password.toCharArray());
			} catch (IOException e) {
				System.out.println("\tError: Keystore ("+Keystore+") load failed.");
				System.exit(1);
			}
		} catch (FileNotFoundException e) {
			System.out.println("\tError: Keystore ("+Keystore+") not found.");
			System.exit(1);
		}

		@SuppressWarnings("rawtypes")
		Enumeration aliasEnumumeration;
		Scanner sc=new Scanner(System.in);
		try {
			aliasEnumumeration = ks.aliases();
			int aNumber =0;
			while (aliasEnumumeration.hasMoreElements()) {
				System.out.println();
				String cAlias = (String) aliasEnumumeration.nextElement();
				if (ks.isKeyEntry(cAlias)) {
					System.out.println("\t|*|Alias (PrivateKey): "+cAlias);
				} else {
					aNumber++;
					System.out.println("\t|"+aNumber+"|Alias: "+cAlias);
				}
				X509Certificate cert1 = (X509Certificate) ks.getCertificate(cAlias);
				System.out.println("\t\tSubject name: "+cert1.getSubjectDN());
				System.out.print("\tDownload above certificate from the keystore '"+Keystore+"'?: [y/n] ");
				String InputGiven=sc.next();
				if (InputGiven.equals("y")) {
					Certificate certificate = ks.getCertificate(cAlias);

				    try {
						File file = new File(cAlias+".cer");
					    byte[] buffer = certificate.getEncoded();
					    FileOutputStream fos = new FileOutputStream(file);
						fos.write(buffer);
					    fos.close();
					    Writer writer = new OutputStreamWriter(fos);
					    writer.write(new sun.misc.BASE64Encoder().encode(buffer));
					    writer.flush();
					} catch (FileNotFoundException e) {
						//e.printStackTrace();
					} catch (IOException e) {
						//e.printStackTrace();
						//System.out.println("\tCertificate.ExtractMain.IOE.");
					}
				    ConvertDERtoBase64.ConvertDERtoBase64Main(cAlias+".cer", 1);
				    System.out.println("\tCertificate "+cAlias+".cer extracted.");
				} else {
					System.out.println("\tCertificate ["+cAlias+"] will be skipped.");
				}
			}
			System.out.println();
		} catch (KeyStoreException e1) {
			System.out.println("\tERROR| ExtractCertificates.java KeystoreException occured.\n");
		}
	}

}