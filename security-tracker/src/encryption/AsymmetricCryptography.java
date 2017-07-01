package encryption;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;


@SuppressWarnings("unused")
public class AsymmetricCryptography {
	private Cipher cipher;
	
	public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException{
		this.cipher = Cipher.getInstance("RSA");
	}
	//https://docs.oracle.com/javase/8/docs/api/java/security.report/spec/PKCS8EncodedKeySpec.html
	public PrivateKey getPrivate(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kfactory = KeyFactory.getInstance("RSA");
		return kfactory.generatePrivate(spec);
	}
	//https://docs.oracle.com/javase/8/docs/api/java/security.report/spec/X509EncodedKeySpec.html
	public PublicKey getPublic(String filename) throws Exception {
		byte[] keyBytes = Files.readAllBytes(new File(filename).toPath());
		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kfactory = KeyFactory.getInstance("RSA");
		return kfactory.generatePublic(spec);
	}
	
	public void encryptFile(byte[] input, File output, PrivateKey key) throws IOException, 
		GeneralSecurityException {
		this.cipher.init(Cipher.ENCRYPT_MODE, key);
		writeToFile(output, this.cipher.doFinal(input));
    }
	
	public void decryptFile(byte[] input, File output, PublicKey key) throws IOException, 
		GeneralSecurityException {
		this.cipher.init(Cipher.DECRYPT_MODE, key);
		writeToFile(output, this.cipher.doFinal(input));
    }
	
	private void writeToFile(File output, byte[] toWrite) throws IllegalBlockSizeException, 
		BadPaddingException, IOException{
		FileOutputStream fileostream = new FileOutputStream(output);
		fileostream.write(toWrite);
		fileostream.flush();
		fileostream.close();
	}
	
	public String encryptText(String msg, PrivateKey key) throws NoSuchAlgorithmException, 
		NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, 
		BadPaddingException, InvalidKeyException{
		this.cipher.init(Cipher.ENCRYPT_MODE, key);
		return Base64.encodeBase64String(cipher.doFinal(msg.getBytes("UTF-8")));
	}
	
	public String decryptText(String msg, PublicKey key) throws InvalidKeyException, 
		UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException{
		this.cipher.init(Cipher.DECRYPT_MODE, key);
		return new String(cipher.doFinal(Base64.decodeBase64(msg)), "UTF-8");
	}
	
	public byte[] getFileInBytes(File filer) throws IOException{
		FileInputStream fileistream = new FileInputStream(filer);
		byte[] fbytes = new byte[(int) filer.length()];
		fileistream.read(fbytes);
		fileistream.close();
		return fbytes;
	}
	
	/*public static void voidmain(String[] args) throws Exception {
		
		AsymmetricCryptography ac = new AsymmetricCryptography();
		PrivateKey privateKey = ac.getPrivate("RSAKeys/private.key");
		PublicKey publicKey = ac.getPublic("RSAKeys/public.key");
		
		String message = "Dallas Cowboys - SuperBowl L2";
		String encrypted_msg = ac.encryptText(message, privateKey);
		String decrypted_msg = ac.decryptText(encrypted_msg, publicKey);
		System.out.println("Original Message: " + message + "\nEncrypted Message: " 
		+ encrypted_msg + "\nDecrypted Message: " + decrypted_msg);
		
		
		if (args[0].equals("enc")) {
		
			BufferedReader br = null;
			FileReader fr = null;
			BufferedWriter bw = null;
			FileWriter fw = null;

			try {

				fr = new FileReader("RSAKeys/0.txt");
				br = new BufferedReader(fr);
				
				fw = new FileWriter("RSAKeys/1.txt");
				bw = new BufferedWriter(fw);
				
				String sCurrentLine;

				br = new BufferedReader(new FileReader("RSAKeys/0.txt"));

				while ((sCurrentLine = br.readLine()) != null) {
					//System.out.println(sCurrentLine);
					String encrypted_msg = ac.encryptText(sCurrentLine, privateKey);
					bw.write(encrypted_msg);
					bw.newLine();;
				}

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (br != null)
						br.close();

					if (fr != null)
						fr.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
				
				try {

					if (bw != null)
						bw.close();

					if (fw != null)
						fw.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}

			}

		}
		
		if (args[0].equals("dec")) {
			
			BufferedReader br0 = null;
			FileReader fr0 = null;
			
			try {

				fr0 = new FileReader("RSAKeys/0.txt");
				br0 = new BufferedReader(fr0);
				
				String sCurrentLine;

				br0 = new BufferedReader(new FileReader("RSAKeys/1.txt"));

				while ((sCurrentLine = br0.readLine()) != null) {
					//System.out.println(sCurrentLine);
					String decrypted_msg = ac.decryptText(sCurrentLine, publicKey);
					//System.out.println(decrypted_msg);
					String name = "cacerts.jks";
				    FileInputStream in = new FileInputStream(name);
				    KeyStore ks = KeyStore.getInstance("JKS");
				    try {
				    	ks.load(in, decrypted_msg.toCharArray());
				    	@SuppressWarnings("rawtypes")
						Enumeration e = ks.aliases();
				    	
				    	while (e.hasMoreElements()) {
				    		
				    		System.out.println(e.nextElement());				    		
				    		Certificate cert = ks.getCertificate(e.nextElement().toString());
				    		
				    	}
				    	break;
				    } catch (IOException ioe) {
				    	//
				    
				    }
					
				}

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {

					if (br0 != null)
						br0.close();

					if (fr0 != null)
						fr0.close();

				} catch (IOException ex) {

					ex.printStackTrace();

				}
				
			}

		}
		
	}*/	

}