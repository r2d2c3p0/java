package security.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import encryption.AsymmetricCryptography;

public class CertificateSearchJSON {

  private List<String> result = new ArrayList<String>();
  
  public List<String> getResult() {	return result; }
  
  public static void main(String[] args) throws Exception {
	System.out.println("Tagged:Certificate Report (JSON) main: Begin");
	CertificateSearchJSON fileSearch = new CertificateSearchJSON();
	fileSearch.searchDirectory(new File(args[0]));
	int count = fileSearch.getResult().size();
	if (count == 0) {
	    System.out.println("\nNo certificates found under " + args[0]);
	} else {
	    System.out.println(" Tagged:Total certificates found " + count + " result!\n");
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(new File("cdump.json"));
			//String resultOriginal = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);						
			ArrayNode mainNode = mapper.createArrayNode();			
			for (String matched : fileSearch.getResult()) {
				FileInputStream fileInputStream = new FileInputStream(matched.toString());
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
				try {
					ArrayNode sanNode = mapper.createArrayNode();						
					ObjectNode certificate1 = mapper.createObjectNode();
					
					String cName = x509Certificate.getSubjectDN().toString();
					Date Exp2 = x509Certificate.getNotAfter();
					BigInteger sNumber = x509Certificate.getSerialNumber();
					String Sig = x509Certificate.getSigAlgName();
					String IssuerDN = x509Certificate.getIssuerDN().toString();
					Date Exp1 = x509Certificate.getNotBefore();
					certificate1.put("common name", cName);
					certificate1.put("serial number", sNumber.toString());
					certificate1.put("valid from", Exp1.toString());
					certificate1.put("valid until", Exp2.toString());
					certificate1.put("signature algorithm", Sig);
					certificate1.put("issuer dn", IssuerDN);
					certificate1.put("file path", matched);					
					certificate1.put("san entries", sanNode);
					// Subjective Alternative Names.
					try {
						Collection<List<?>> SANEntries = x509Certificate.getSubjectAlternativeNames();
						for (List<?> SAN: SANEntries) {
							 ObjectNode certificatesan1 = mapper.createObjectNode();
							 //System.out.println("\t"+SAN.get(1));
							 certificatesan1.put("name", SAN.get(1).toString());
							 sanNode.add(certificatesan1);
						}
						//System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+SANEntries+Sig);
						
					} catch (CertificateParsingException e1) {
						// TODO Auto-generated catch block
						//System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+null+Sig);
						ObjectNode certificatesan1 = mapper.createObjectNode();
						certificatesan1.put("name", "Null");
						sanNode.add(certificatesan1);
					}					
					mainNode.add(certificate1);
					
				} catch (Exception e) {
						
						AsymmetricCryptography ac = new AsymmetricCryptography();
						PublicKey publicKey = ac.getPublic("RSAKeys/public.key");
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
								String name = matched;
							    FileInputStream in = new FileInputStream(name);
							    KeyStore ks = KeyStore.getInstance("JKS");
							    try {
							    	ks.load(in, decrypted_msg.toCharArray());
							    	@SuppressWarnings("rawtypes")
									Enumeration kenum = ks.aliases();
							    	
							    	while (kenum.hasMoreElements()) {
							    		
							    		System.out.println(kenum.nextElement());				    		
							    		Certificate cert = ks.getCertificate(kenum.nextElement().toString());
							    		//if(cert instanceof X509Certificate) {
						                X509Certificate ksAliasCertificate = (X509Certificate) cert;
						                //System.out.println(x.getIssuerDN());
							    		//}
							    		
										ArrayNode sanNode = mapper.createArrayNode();						
										ObjectNode certificate1 = mapper.createObjectNode();
										
										String cName = ksAliasCertificate.getSubjectDN().toString();
										Date Exp2 = ksAliasCertificate.getNotAfter();
										BigInteger sNumber = ksAliasCertificate.getSerialNumber();
										String Sig = ksAliasCertificate.getSigAlgName();
										String IssuerDN = ksAliasCertificate.getIssuerDN().toString();
										Date Exp1 = ksAliasCertificate.getNotBefore();
										certificate1.put("common name", cName);
										certificate1.put("serial number", sNumber.toString());
										certificate1.put("valid from", Exp1.toString());
										certificate1.put("valid until", Exp2.toString());
										certificate1.put("signature algorithm", Sig);
										certificate1.put("issuer dn", IssuerDN);
										certificate1.put("file path", matched);					
										certificate1.put("san entries", sanNode);
										// Subjective Alternative Names.
										try {
											Collection<List<?>> SANEntries = ksAliasCertificate.getSubjectAlternativeNames();
											for (List<?> SAN: SANEntries) {
												 ObjectNode certificatesan1 = mapper.createObjectNode();
												 //System.out.println("\t"+SAN.get(1));
												 certificatesan1.put("name", SAN.get(1).toString());
												 sanNode.add(certificatesan1);
											}
											//System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+SANEntries+Sig);
											
										} catch (CertificateParsingException e1) {
											// TODO Auto-generated catch block
											//System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+null+Sig);
											ObjectNode certificatesan1 = mapper.createObjectNode();
											certificatesan1.put("name", "Null [CPE]");
											sanNode.add(certificatesan1);
										} catch (NullPointerException ne1) {
											ObjectNode certificatesan1 = mapper.createObjectNode();
											certificatesan1.put("name", "Null [NPE]");
											sanNode.add(certificatesan1);
										}					
										mainNode.add(certificate1);
							    	}
							    	break;
							    } catch (IOException ioe) {
							    	//
							    }								
							}
						} catch (IOException kioe) {
							kioe.printStackTrace();
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
					
					//System.out.println(" Tagged:Not a certificate - " + matched);
					
				}
				
				
			}
			((ObjectNode) root).set("Certificate Report", mainNode);			
			String resultUpdate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
			System.out.println("\n" + resultUpdate + "\n");
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	System.out.println("Tagged:Certificate Report (JSON) main: End");
  }

  public void searchDirectory(File directory) {
	if (directory.isDirectory()) {
	    search(directory);
	} else {
	    System.out.println(directory.getAbsoluteFile() + " is not a directory!");
	}
  }

  private void search(File file) {
	if (file.isDirectory()) {
		System.out.println("\n Tagged: Searching directory : [" + file.getAbsoluteFile() + "]");            
	    if (file.canRead()) {
	    	for (File temp : file.listFiles()) {
	    		if (temp.isDirectory()) {	    			
	    			search(temp);
	    		} else {
	    			if (temp.getName().endsWith(".jks") || temp.getName().endsWith(".pem") || temp.getName().endsWith(".cer") || 
	    					temp.getName().endsWith(".crt")) {
	    				result.add(temp.getAbsoluteFile().toString());
	    			}	    			
	    		}
	    	}
	    } else {
		System.out.println("Tagged: " + file.getAbsoluteFile() + " access denied");
	 }
   }
  }  
  
}