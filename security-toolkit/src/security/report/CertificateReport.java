package security.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import encryption.AsymmetricCryptography;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.SimpleDateFormat;

public class CertificateReport {

	private static List<String> result = new ArrayList<String>();
	
	private static final long MM_SECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	
	private static int EXPIRES_IN_LESS_THAN_15DAYS = 0;
	private static int EXPIRES_IN_LESS_THAN_30DAYS = 0;
	private static int EXPIRES_IN_LESS_THAN_60DAYS = 0;
	private static int EXPIRES_IN_LESS_THAN_90DAYS = 0;
	private static int EXPIRES_IN_LESS_THAN_0DAYS = 0;
	private static int GOOD_STANDING = 0;
	
	private static final Date curDate = new Date();
	private static final long curTime = curDate.getTime();

	public List<String> getResult() {
		return result;
	}

	public static String convertTime(long time){
	    Date date = new Date(time);
	    //Format format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
	    Format format = new SimpleDateFormat("yyyy-MM-dd");
	    return format.format(date);
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
		CertificateReport fileSearch = new CertificateReport();
		fileSearch.searchDirectory(new File(args[0]));
		int filecount = fileSearch.getResult().size();
		if (filecount == 0) {
			System.out.println("\nNo certificates / keystores found under " + args[0]);
		} else {
			System.out.println("\n Tagged:Total files found " + filecount + ". \n");
			try {
				System.out.println("Tagged:Certificate Report (security.report.main): Begin");
				ObjectMapper mapper = new ObjectMapper();
				//ObjectMapper mapper1 = new ObjectMapper();
				JsonNode root = mapper.readTree(new File("common/cdump.json"));			
				// String resultOriginal =
				// mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
				ArrayNode mainNode = mapper.createArrayNode();
				//ArrayNode main1Node = mapper1.createArrayNode();
				ObjectNode timelines = mapper.createObjectNode();
				PrintWriter csvw = new PrintWriter(new File("crs-output/crs.csv"));
		        StringBuilder csvb = new StringBuilder();
		        
		        csvb.append("commonname");
		        csvb.append(',');
		        csvb.append("serialnumber");
		        csvb.append(',');
		        csvb.append("validfrom");
		        csvb.append(',');
		        csvb.append("validuntil");
		        csvb.append(',');
		        csvb.append("filepath");
		        csvb.append('\n');
		        
				for (String matched : fileSearch.getResult()) {
					if (matched.endsWith(".jks") || matched.endsWith(".JKS")) {
						int passwordfound = 0;
						// System.out.println(matched);
						AsymmetricCryptography ac = new AsymmetricCryptography();
						try {							
							PublicKey publicKey = ac.getPublic("common/unlock-public.key");
							BufferedReader br0 = null;
							FileReader fr0 = null;
							try {
								String sCurrentLine = null;
								String name = matched;
								FileInputStream in = new FileInputStream(name);
								KeyStore ks = KeyStore.getInstance("JKS");
								fr0 = new FileReader("common/unlock.key");
								br0 = new BufferedReader(fr0);
								br0 = new BufferedReader(new FileReader("common/unlock.key"));
								while ((sCurrentLine = br0.readLine()) != null) {
									// System.out.println(sCurrentLine);
									String decrypted_msg = ac.decryptText(sCurrentLine, publicKey).trim();
									// System.out.println(decrypted_msg);
									try {
										ks.load(in, decrypted_msg.toCharArray());
										@SuppressWarnings("rawtypes")
										Enumeration kenum = ks.aliases();
										while (kenum.hasMoreElements()) {
											// System.out.println(kenum.nextElement());
											Certificate cert = ks.getCertificate(kenum.nextElement().toString());
											// if(cert instanceof X509Certificate) {
											X509Certificate ksAliasCertificate = (X509Certificate) cert;
											// System.out.println(x.getIssuerDN());
											// }
											ArrayNode sanNode = mapper.createArrayNode();
											ObjectNode certificate3 = mapper.createObjectNode();
											String cName = ksAliasCertificate.getSubjectDN().toString();
											Date Exp2 = ksAliasCertificate.getNotAfter();
											BigInteger sNumber = ksAliasCertificate.getSerialNumber();
											String Sig = ksAliasCertificate.getSigAlgName();
											String IssuerDN = ksAliasCertificate.getIssuerDN().toString();
											Date Exp1 = ksAliasCertificate.getNotBefore();
											
											long expTime = Exp2.getTime();
									        int dayOffSet = (int) ((expTime - curTime) / MM_SECONDS_PER_DAY);									        
									        								        
									        if (dayOffSet <= 0) {
									        	EXPIRES_IN_LESS_THAN_0DAYS++;
									        	//System.out.println(dayOffSet + " 0---- " + EXPIRES_IN_LESS_THAN_0DAYS);
									        } else {
									        	if (dayOffSet < 15) {
									        		EXPIRES_IN_LESS_THAN_15DAYS++;
									        		//System.out.println(dayOffSet + " 15---- " + EXPIRES_IN_LESS_THAN_15DAYS);
									        	} else {
									        		if (dayOffSet < 30) {
									        			EXPIRES_IN_LESS_THAN_30DAYS++;
									        			//System.out.println(dayOffSet + " 30---- " + EXPIRES_IN_LESS_THAN_30DAYS);
									        		} else {
									        			if (dayOffSet < 60) {
									        				EXPIRES_IN_LESS_THAN_60DAYS++;
									        				//System.out.println(dayOffSet + " 60---- " + EXPIRES_IN_LESS_THAN_60DAYS);
									        			} else {
									        				if (dayOffSet < 90) {
									        					EXPIRES_IN_LESS_THAN_90DAYS++;
									        					//System.out.println(dayOffSet + " 90---- " + EXPIRES_IN_LESS_THAN_90DAYS);
									        				} else {
									        					GOOD_STANDING++;
									        					//System.out.println(dayOffSet + " 90+---- " + GOOD_STANDING);
									        				}
									        			}									        	
									        		}
									        	}
									        }									        									        
									        
											certificate3.put("commonname", cName);
											certificate3.put("serialnumber", sNumber.toString());
											certificate3.put("validfrom", convertTime(Exp1.getTime()));
											certificate3.put("validuntil", convertTime(Exp2.getTime()));
											certificate3.put("signaturealgorithm", Sig);
											certificate3.put("issuerdn", IssuerDN);
											certificate3.put("filepath", matched);
											certificate3.put("activedays", dayOffSet);										
											certificate3.put("sanentries", sanNode);
											
											// CSV
											csvb.append(cName.replaceAll("\\s","-").replaceAll(",", ""));
									        csvb.append(',');
									        csvb.append(sNumber);
									        csvb.append(',');
									        csvb.append(convertTime(Exp1.getTime()));
									        csvb.append(',');
									        csvb.append(convertTime(Exp2.getTime()));
									        csvb.append(',');
									        csvb.append(matched);
									        csvb.append('\n');
									        
											// Subjective Alternative Names.
											try {
												Collection<List<?>> SANEntries = ksAliasCertificate
														.getSubjectAlternativeNames();
												for (List<?> SAN : SANEntries) {
													ObjectNode certificatesan3 = mapper.createObjectNode();
													// System.out.println("\t"+SAN.get(1));
													certificatesan3.put("name", SAN.get(1).toString());
													sanNode.add(certificatesan3);
												}
												// System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+SANEntries+Sig);
											} catch (CertificateParsingException e1) {
												// TODO Auto-generated catch// block
												// System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+null+Sig);
												ObjectNode certificatesan3 = mapper.createObjectNode();
												certificatesan3.put("name", "Null [CPE]");
												sanNode.add(certificatesan3);
											} catch (NullPointerException ne1) {
												ObjectNode certificatesan3 = mapper.createObjectNode();
												certificatesan3.put("name", "Null [NPE]");
												sanNode.add(certificatesan3);
											}
											mainNode.add(certificate3);
										}
										passwordfound = 1;
										break;
									} catch (IOException ioe) {
										// No needSystem.out.println("// Tagged:PKCS12 " + alias + " load // failed");
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
						} catch (Exception e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
							System.out.println("  Tagged:untrusted execution.");
							System.exit(127);
						}
						if (passwordfound == 0) {
							// System.out.println(" Tagged:Not a certificate - "// + matched);
							System.out.println("  Tagged:Password help for " + matched);
						}
					} else if (matched.endsWith(".p12") || matched.endsWith(".PFX") || matched.endsWith(".pfx") || matched.endsWith(".P12")) {
						int passwordfound = 0;
						// System.out.println(matched);
						AsymmetricCryptography ac = new AsymmetricCryptography();
						try {
							PublicKey publicKey = ac.getPublic("common/unlock-public.key");
							BufferedReader br0 = null;
							FileReader fr0 = null;
							try {
								String sCurrentLine = null;
								String name = matched;
								FileInputStream in = new FileInputStream(name);
								KeyStore p12ks = KeyStore.getInstance("PKCS12");
								fr0 = new FileReader("common/unlock.key");
								br0 = new BufferedReader(fr0);
								br0 = new BufferedReader(new FileReader("common/unlock.key"));
								while ((sCurrentLine = br0.readLine()) != null) {
									// System.out.println(sCurrentLine);
									String decrypted_msg = ac.decryptText(sCurrentLine, publicKey).trim();									
									//System.out.println(decrypted_msg);
									try {
										p12ks.load(in, decrypted_msg.toCharArray());
										@SuppressWarnings("rawtypes")
										Enumeration pkenum = p12ks.aliases();
										while (pkenum.hasMoreElements()) {
											// System.out.println(kenum.nextElement());
											Certificate pcert = p12ks.getCertificate(pkenum.nextElement().toString());
											// if(cert instanceof X509Certificate) {
											X509Certificate pksAliasCertificate = (X509Certificate) pcert;
											// System.out.println(x.getIssuerDN());
											// }
											ArrayNode sanNode = mapper.createArrayNode();
											ObjectNode certificate2 = mapper.createObjectNode();
											String cName = pksAliasCertificate.getSubjectDN().toString();
											Date Exp2 = pksAliasCertificate.getNotAfter();
											BigInteger sNumber = pksAliasCertificate.getSerialNumber();
											String Sig = pksAliasCertificate.getSigAlgName();
											String IssuerDN = pksAliasCertificate.getIssuerDN().toString();
											Date Exp1 = pksAliasCertificate.getNotBefore();
											
											long expTime = Exp2.getTime();
									        int dayOffSet = (int) ((expTime - curTime) / MM_SECONDS_PER_DAY);

									        if (dayOffSet <= 0) {
									        	EXPIRES_IN_LESS_THAN_0DAYS++;
									        	//System.out.println(dayOffSet + " 0---- " + EXPIRES_IN_LESS_THAN_0DAYS);
									        } else {
									        	if (dayOffSet < 15) {
									        		EXPIRES_IN_LESS_THAN_15DAYS++;
									        		//System.out.println(dayOffSet + " 15---- " + EXPIRES_IN_LESS_THAN_15DAYS);
									        	} else {
									        		if (dayOffSet < 30) {
									        			EXPIRES_IN_LESS_THAN_30DAYS++;
									        			//System.out.println(dayOffSet + " 30---- " + EXPIRES_IN_LESS_THAN_30DAYS);
									        		} else {
									        			if (dayOffSet < 60) {
									        				EXPIRES_IN_LESS_THAN_60DAYS++;
									        				//System.out.println(dayOffSet + " 60---- " + EXPIRES_IN_LESS_THAN_60DAYS);
									        			} else {
									        				if (dayOffSet < 90) {
									        					EXPIRES_IN_LESS_THAN_90DAYS++;
									        					//System.out.println(dayOffSet + " 90---- " + EXPIRES_IN_LESS_THAN_90DAYS);
									        				} else {
									        					GOOD_STANDING++;
									        					//System.out.println(dayOffSet + " 90+---- " + GOOD_STANDING);
									        				}
									        			}									        	
									        		}
									        	}
									        }
									        
											certificate2.put("commonname", cName);
											certificate2.put("serialnumber", sNumber.toString());
											certificate2.put("validfrom", convertTime(Exp1.getTime()));
											certificate2.put("validuntil", convertTime(Exp2.getTime()));
											certificate2.put("signaturealgorithm", Sig);
											certificate2.put("issuerdn", IssuerDN);
											certificate2.put("filepath", matched);
											certificate2.put("activedays", dayOffSet);											
											certificate2.put("sanentries", sanNode);
											
											// CSV
											csvb.append(cName.replaceAll("\\s","-").replaceAll(",", ""));
									        csvb.append(',');
									        csvb.append(sNumber);
									        csvb.append(',');
									        csvb.append(convertTime(Exp1.getTime()));
									        csvb.append(',');
									        csvb.append(convertTime(Exp2.getTime()));
									        csvb.append(',');
									        csvb.append(matched);
									        csvb.append('\n');
									        
											// Subjective Alternative Names.
											try {
												Collection<List<?>> SANEntries = pksAliasCertificate.getSubjectAlternativeNames();
												for (List<?> SAN : SANEntries) {
													ObjectNode certificatesan2 = mapper.createObjectNode();
													// System.out.println("\t"+SAN.get(1));
													certificatesan2.put("name", SAN.get(1).toString());
													sanNode.add(certificatesan2);
												}
												// System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+SANEntries+Sig);

											} catch (CertificateParsingException e1) {
												// TODO Auto-generated catch
												// block
												// System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+null+Sig);
												ObjectNode certificatesan2 = mapper.createObjectNode();
												certificatesan2.put("name", "Null [CPE]");
												sanNode.add(certificatesan2);
											} catch (NullPointerException ne1) {
												ObjectNode certificatesan2 = mapper.createObjectNode();
												certificatesan2.put("name", "Null [NPE]");
												sanNode.add(certificatesan2);
											}
											mainNode.add(certificate2);
										}
										passwordfound = 1;
										break;
									} catch (IOException ioe) {
										// No needSystem.out.println("// Tagged:PKCS12 " + alias + " load // failed");
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
						} catch (Exception e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
							System.out.println("  Tagged:untrusted execution.");
							System.exit(127);
						}
						if (passwordfound == 0) {
							// System.out.println(" Tagged:Not a certificate - " + matched);
							System.out.println("  Tagged:Password help for " + matched);
						}
					} else {
						FileInputStream fileInputStream = new FileInputStream(matched.toString());
						CertificateFactory certificateFactory = null;
						try {
							certificateFactory = CertificateFactory.getInstance("X509");
						} catch (CertificateException Ce) {
							// e.printStackTrace();
							System.out.println("  Tagged:Failed (CertificateException1) to parse the certificate "
									+ matched.toString());
						}
						X509Certificate x509Certificate = null;
						try {
							x509Certificate = (X509Certificate) certificateFactory.generateCertificate(fileInputStream);
						} catch (CertificateException Ce) {
							System.out.println("  Tagged:Private key " + matched.toString());
						}
						try {
							ArrayNode sanNode = mapper.createArrayNode();
							ObjectNode certificate1 = mapper.createObjectNode();
							//Date curDate = new Date(); 
						    //long curTime = curDate.getTime();
							String cName = x509Certificate.getSubjectDN().toString();
							Date Exp2 = x509Certificate.getNotAfter();
							BigInteger sNumber = x509Certificate.getSerialNumber();
							String Sig = x509Certificate.getSigAlgName();
							String IssuerDN = x509Certificate.getIssuerDN().toString();
							Date Exp1 = x509Certificate.getNotBefore();
							
							//System.out.println(convertTime(Exp2.getTime()));
							long expTime = Exp2.getTime();
					        int dayOffSet = (int) ((expTime - curTime) / MM_SECONDS_PER_DAY);

					        if (dayOffSet <= 0) {
					        	EXPIRES_IN_LESS_THAN_0DAYS++;
					        	//System.out.println(dayOffSet + " 0---- " + EXPIRES_IN_LESS_THAN_0DAYS);
					        } else {
					        	if (dayOffSet < 15) {
					        		EXPIRES_IN_LESS_THAN_15DAYS++;
					        		//System.out.println(dayOffSet + " 15---- " + EXPIRES_IN_LESS_THAN_15DAYS);
					        	} else {
					        		if (dayOffSet < 30) {
					        			EXPIRES_IN_LESS_THAN_30DAYS++;
					        			//System.out.println(dayOffSet + " 30---- " + EXPIRES_IN_LESS_THAN_30DAYS);
					        		} else {
					        			if (dayOffSet < 60) {
					        				EXPIRES_IN_LESS_THAN_60DAYS++;
					        				//System.out.println(dayOffSet + " 60---- " + EXPIRES_IN_LESS_THAN_60DAYS);
					        			} else {
					        				if (dayOffSet < 90) {
					        					EXPIRES_IN_LESS_THAN_90DAYS++;
					        					//System.out.println(dayOffSet + " 90---- " + EXPIRES_IN_LESS_THAN_90DAYS);
					        				} else {
					        					GOOD_STANDING++;
					        					//System.out.println(dayOffSet + " 90+---- " + GOOD_STANDING);
					        				}
					        			}									        	
					        		}
					        	}
					        }					        
					        
							// JSON code here
							certificate1.put("commonname", cName);
							certificate1.put("serialnumber", sNumber.toString());
							certificate1.put("validfrom", convertTime(Exp1.getTime()));
							certificate1.put("validuntil", convertTime(Exp2.getTime()));
							certificate1.put("signaturealgorithm", Sig);
							certificate1.put("issuerdn", IssuerDN);
							certificate1.put("filepath", matched);
							certificate1.put("activedays", dayOffSet);							
							certificate1.put("sanentries", sanNode);
							
							// CSV
							csvb.append(cName.replaceAll("\\s","-").replaceAll(",", ""));
					        csvb.append(',');
					        csvb.append(sNumber);
					        csvb.append(',');
					        csvb.append(convertTime(Exp1.getTime()));
					        csvb.append(',');
					        csvb.append(convertTime(Exp2.getTime()));
					        csvb.append(',');
					        csvb.append(matched);
					        csvb.append('\n');

							// Subjective Alternative Names.
							try {
								Collection<List<?>> SANEntries = x509Certificate.getSubjectAlternativeNames();
								for (List<?> SAN : SANEntries) {
									ObjectNode certificatesan1 = mapper.createObjectNode();
									// System.out.println("\t"+SAN.get(1));
									certificatesan1.put("name", SAN.get(1).toString());
									sanNode.add(certificatesan1);
								}
								// System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+SANEntries+Sig);
							} catch (CertificateParsingException e1) {
								// TODO Auto-generated catch block
								// System.out.println(cName+IssuerDN+sNumber+Exp1+Exp2+null+Sig);
								ObjectNode certificatesan1 = mapper.createObjectNode();
								certificatesan1.put("name", "Null");
								sanNode.add(certificatesan1);
							}
							mainNode.add(certificate1);
						} catch (NullPointerException npe) {
							System.out.println("  Tagged:Certificate " + matched + " error [NPE].");
						}
					}
				}
				timelines.put("90PlusDays", GOOD_STANDING);
				timelines.put("LessThan90Days", EXPIRES_IN_LESS_THAN_90DAYS);
				timelines.put("LessThan60Days", EXPIRES_IN_LESS_THAN_60DAYS);
				timelines.put("LessThan30Days", EXPIRES_IN_LESS_THAN_30DAYS);
				timelines.put("LessThan15Days", EXPIRES_IN_LESS_THAN_15DAYS);
				timelines.put("Expired", EXPIRES_IN_LESS_THAN_0DAYS);
				mainNode.add(timelines);
				((ObjectNode) root).set("Certificate-Report", mainNode);
				String resultUpdate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
				if (args.length <= 1) {
					//Nothing					
				} else {
					if (args[1].equals("json")) {
						BufferedWriter jsonbw = null;
						FileWriter jsonfw = null;
						try {
							jsonfw = new FileWriter("crs-output/crs.json");
							jsonbw = new BufferedWriter(jsonfw);
							jsonbw.write(resultUpdate);
							jsonbw.newLine();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								if (jsonbw != null)
									jsonbw.close();
								if (jsonfw != null)
									jsonfw.close();
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							System.out.println("  Tagged:Check crs-out/crs.json for JSON output.");
							//System.out.println("\n\n" + resultUpdate + "\n");
						}
					} else if (args[1].equals("csv")) {
						System.out.println("  Tagged:Check crs-out/crs.csv for CSV output.");
					}
				}
				csvw.write(csvb.toString());
		        csvw.close();
			} catch (JsonGenerationException jge) {
				jge.printStackTrace();
			} catch (JsonMappingException jme) {
				jme.printStackTrace();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}			
			/*System.out.println("15 days " + EXPIRES_IN_LESS_THAN_15DAYS);
			System.out.println("30 days " + EXPIRES_IN_LESS_THAN_30DAYS);
			System.out.println("60 days " + EXPIRES_IN_LESS_THAN_60DAYS);
			System.out.println("90 days " + EXPIRES_IN_LESS_THAN_90DAYS);
			System.out.println("0 days " + EXPIRES_IN_LESS_THAN_0DAYS);
			System.out.println("All clear " + GOOD_STANDING);
			*/
			System.out.println("Tagged:Certificate Report (security.report.main): End");
		}

	}

	public void searchDirectory(File directory) {
		if (directory.isDirectory()) {
			search(directory);
		} else {
			System.out.println(directory.getAbsoluteFile() + " is NOT a valid directory/ path !");
		}
	}

	private void search(File file) {

		if (file.isDirectory()) {
			System.out.println(" Tagged:Searching directory : [" + file.getAbsoluteFile() + "]");
			if (file.canRead()) {
				for (File filetmp : file.listFiles()) {
					if (filetmp.isDirectory()) {
						search(filetmp);
					} else {
						if (filetmp.getName().endsWith(".arm") || filetmp.getName().endsWith(".ARM")
								|| filetmp.getName().endsWith(".cer") || filetmp.getName().endsWith(".CER")
								|| filetmp.getName().endsWith(".pem") || filetmp.getName().endsWith(".PEM")
								|| filetmp.getName().endsWith(".jks") || filetmp.getName().endsWith(".JKS")
								|| filetmp.getName().endsWith(".der") || filetmp.getName().endsWith(".DER")
								|| filetmp.getName().endsWith(".pfx") || filetmp.getName().endsWith(".PFX")
								|| filetmp.getName().endsWith(".crt") || filetmp.getName().endsWith(".CRT")) {
							result.add(filetmp.getAbsoluteFile().toString());
						}
					}
				}
			} else {
				System.out.println(" Tagged:" + file.getAbsoluteFile() + " access denied.");
			}
		}

	}

}