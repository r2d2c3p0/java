package custom.ssl.tool;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.GeneralSecurityException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

class KeyImportException extends Exception  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public KeyImportException(String msg)  {
    super(msg);
  }
}

/**
 * Import an existing key into a keystore.  This creates a key entry as though you had
 * submitted a "genkey" request so that a subsequent "import" command will import the
 * certificate correctly.
 */
public class KeyImport  {
	
  private static final int invCodes[] = {
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, 64, -1, -1,
    -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1
  };

  private static byte[] OID_RSA_FORMAT = { (byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, 
       (byte) 0x0D, (byte) 0x01, (byte) 0x01, (byte) 0x01 };
  private static byte[] OID_DSA_FORMAT = { (byte) 0x2A, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xF7, 
       (byte) 0x0D, (byte) 0x01, (byte) 0x01, (byte) 0x02 }; 

  private static byte[] base64Decode(String input)    {
    if (input.length() % 4 != 0)    {
        throw new IllegalArgumentException("Invalid base64 input");
    }
    byte decoded[] = new byte[((input.length() * 3) / 4) - 
      (input.indexOf('=') > 0 ? (input.length() - input.indexOf('=')) : 0)];
    char[] inChars = input.toCharArray();
    int j = 0;
    int b[] = new int[4];
    for (int i = 0; i < inChars.length; i += 4)     {
      b[0] = invCodes[inChars[i]];
      b[1] = invCodes[inChars[i + 1]];
      b[2] = invCodes[inChars[i + 2]];
      b[3] = invCodes[inChars[i + 3]];
      decoded[j++] = (byte) ((b[0] << 2) | (b[1] >> 4));
      if (b[2] < 64)      {
        decoded[j++] = (byte) ((b[1] << 4) | (b[2] >> 2));
        if (b[3] < 64)  {
          decoded[j++] = (byte) ((b[2] << 6) | b[3]);
        }
      }
    }

    return decoded;
  }

  /**
   * Bare-bones ASN.1 parser that can only deal with a structure that contains integers
   * (as I expect for the RSA private key format given in PKCS #1 and RFC 3447).
   * @param b the bytes to be parsed as ASN.1 DER
   * @param integers an output array to which all integers encountered during the parse
   *   will be appended in the order they're encountered.  It's up to the caller to determine
   *   which is which.
   * @param oids an output array of all 0x06 tags
   * @param byteStrings an output array of all 0x04 tags
   */
  private static void ASN1Parse(byte[] b, 
		  List<BigInteger> integers,
		  List<byte[]> oids,
		  List<byte[]> byteStrings) throws KeyImportException  {
    int pos = 0;
    while (pos < b.length)  {
      byte tag = b[pos++];
      int length = b[pos++];
      if ((length & 0x80) != 0)  {
        int extLen = 0;
        for (int i = 0; i < (length & 0x7F); i++)  {
          extLen = (extLen << 8) | (b[pos++] & 0xFF);
        }
        length = extLen;
      }
      byte[] contents = new byte[length];
      System.arraycopy(b, pos, contents, 0, length);
      pos += length;

      if (tag == 0x30)  {  // sequence
        ASN1Parse(contents, integers, oids, byteStrings);
      } else if (tag == 0x02)  {  // Integer
        BigInteger i = new BigInteger(contents);
        integers.add(i);
      } else if (tag == 0x04)  { // byte string
        byteStrings.add(contents);
      } else if (tag == 0x06) { // OID
        oids.add(contents);
      } else if (tag == 0x05)  { // String
        // Ignore this.  It comes up in the RSA format, but only as a placeholder.
      } else  {
        throw new KeyImportException("Unsupported ASN.1 tag " + tag + " encountered.  Is this a " +
          "valid RSA key?");
      }
    }
  }

  private static byte[] stretchKey(String password, byte[] salt, int iterationCount)
      throws GeneralSecurityException  {
    KeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), 
      salt,
      iterationCount,
      192);    // length of a DES3 key
    SecretKeyFactory fact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

    return fact.generateSecret(pbeKeySpec).getEncoded();
  }

  private static byte[] decrypt(byte[] key, byte[] iv, byte[] encrypted) 
      throws GeneralSecurityException  {
    DESedeKeySpec desKeySpec = new DESedeKeySpec(key);
    SecretKeySpec desKey = new SecretKeySpec(desKeySpec.getKey(), "DESede");
    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    IvParameterSpec ivSpec = new IvParameterSpec(iv);
    cipher.init(Cipher.DECRYPT_MODE, desKey, ivSpec);
    return cipher.doFinal(encrypted);
  }

  /**
   * Read a PKCS #8, Base64-encrypted file as a Key instance.  This handles encrypted or
   * decrypted and RSA or DSA.  It only accepts Base64 encoded input (i.e. not DER).
   */
  private static PrivateKey readPrivateKeyFile(String keyFileName, String keyFilePassword) 
      throws IOException, GeneralSecurityException, KeyImportException {
	  
    BufferedReader in = new BufferedReader(new FileReader(keyFileName));
    
    try  {
      String line;
      boolean encrypted = false;
      boolean readingKey = false;
      boolean pkcs8Format = false;
      boolean rsaFormat = false;
      boolean dsaFormat = false;
      StringBuffer base64EncodedKey = new StringBuffer();
      
      while ((line = in.readLine()) != null)  {
    	  
        if (readingKey)  {
          if (line.trim().equals("-----END RSA PRIVATE KEY-----"))  {  // PKCS #1
            readingKey = false;
          } else if (line.trim().equals("-----END DSA PRIVATE KEY-----"))  {
            readingKey = false;
          } else if (line.trim().equals("-----END PRIVATE KEY-----"))  {  // PKCS #8
            readingKey = false;
          } else if (line.trim().equals("-----END ENCRYPTED PRIVATE KEY-----"))  {
            readingKey = false;
          } else  {
            base64EncodedKey.append(line.trim());
          }
        } else if  (line.trim().equals("-----BEGIN RSA PRIVATE KEY-----"))  {
          readingKey = true;
          rsaFormat = true;
        } else if  (line.trim().equals("-----BEGIN DSA PRIVATE KEY-----"))  {
          readingKey = true;
          dsaFormat = true;
        } else if  (line.trim().equals("-----BEGIN PRIVATE KEY-----"))  {
          readingKey = true;
          pkcs8Format = true;
        } else if  (line.trim().equals("-----BEGIN ENCRYPTED PRIVATE KEY-----"))  {
          readingKey = true;
          encrypted = true;
        }
      }
      
      if (base64EncodedKey.length() == 0) {
        throw new IOException("File '" + keyFileName + 
          "' did not contain an unencrypted private key");
      }

      byte[] bytes = base64Decode(base64EncodedKey.toString());

      if (encrypted) {
        List<BigInteger> pkcs5Integers = new ArrayList<BigInteger>();
        List<byte[]> oids = new ArrayList<byte[]>();
        List<byte[]> byteStrings = new ArrayList<byte[]>();
        ASN1Parse(bytes, pkcs5Integers, oids, byteStrings);

        byte[] salt = byteStrings.get(0);
        int iterationCount = pkcs5Integers.get(0).intValue();

        if (keyFilePassword == null)  {
          throw new KeyImportException("This is an encrypted key file.  An -importPassword is required");
        }

        // XXX I should be verifying the key-stretching algorithm OID here
        byte[] key = stretchKey(keyFilePassword, salt, iterationCount);
        byte[] encryptedBytes = byteStrings.get(2);
        byte[] iv = byteStrings.get(1);
        // XXX I should be verifying the encryption algorithm OID here
        bytes = decrypt(key, iv, encryptedBytes);

        // Parse the de-crypted output to determine its type (RSA or DSA)
        pkcs5Integers = new ArrayList<BigInteger>();
        oids = new ArrayList<byte[]>();
        byteStrings = new ArrayList<byte[]>();
        ASN1Parse(bytes, pkcs5Integers, oids, byteStrings);

        if (Arrays.equals(oids.get(0), OID_RSA_FORMAT))  {
          bytes = byteStrings.get(0);
          rsaFormat = true;
        } else if (Arrays.equals(oids.get(0), OID_DSA_FORMAT))  {
          bytes = byteStrings.get(0);
          dsaFormat = true;
        } else  {
          System.out.println("Unrecognized key format");
          System.exit(0);
        }
      }

      // PKCS #8 as in: http://www.agentbob.info/agentbob/79-AB.html
      KeyFactory kf = null;
      KeySpec spec = null;
      
      if (pkcs8Format)  {
        kf = KeyFactory.getInstance("RSA");
        spec = new PKCS8EncodedKeySpec(bytes);
      } else if (rsaFormat)  {
        kf = KeyFactory.getInstance("RSA");
        List<BigInteger> rsaIntegers = new ArrayList<BigInteger>();
        ASN1Parse(bytes, rsaIntegers, null, null);
        if (rsaIntegers.size() < 8)  {
          throw new KeyImportException("'" + keyFileName + 
            "' does not appear to be a properly formatted RSA key");
        }
        BigInteger publicExponent = rsaIntegers.get(2);
        BigInteger privateExponent = rsaIntegers.get(3);
        BigInteger modulus = rsaIntegers.get(1);
        BigInteger primeP = rsaIntegers.get(4);
        BigInteger primeQ = rsaIntegers.get(5);
        BigInteger primeExponentP = rsaIntegers.get(6);
        BigInteger primeExponentQ = rsaIntegers.get(7);
        BigInteger crtCoefficient = rsaIntegers.get(8);
        spec = new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent,
          primeP, primeQ, primeExponentP, primeExponentQ, crtCoefficient);
      } else if (dsaFormat)  {
        kf = KeyFactory.getInstance("DSA");
        List<BigInteger> dsaIntegers = new ArrayList<BigInteger>();
        ASN1Parse(bytes, dsaIntegers, null, null);
        if (dsaIntegers.size() < 5)  {
          throw new KeyImportException("'" + keyFileName + 
            "' does not appear to be a properly formatted DSA key");
        }
        BigInteger privateExponent = dsaIntegers.get(1);
        @SuppressWarnings("unused")
		BigInteger publicExponent = dsaIntegers.get(2);
        BigInteger P = dsaIntegers.get(3);
        BigInteger Q = dsaIntegers.get(4);
        BigInteger G = dsaIntegers.get(5);
        spec = new DSAPrivateKeySpec(privateExponent, P, Q, G);
      }
      return kf.generatePrivate(spec);
    } finally  {
      in.close();
    }
    
  }

  static class Parameter {
	  
    String flag;
    boolean required;
    String description;
    String defaultValue;

    public Parameter(String flag, boolean required, String description, String defaultValue) {
    	
      this.flag = flag;
      this.required = required;
      this.description = description;
      this.defaultValue = defaultValue;
      
    }

    public boolean equals(Object o) {
    	
      return (o instanceof Parameter) && (this.flag.equals(((Parameter) o).flag));
      
    }
  }

  private static String KEY_FILE = "-keyFile";
  private static String ALIAS = "-alias";
  private static String CERT_FILE = "-certificateFile";
  private static String KEY_STORE = "-keystore";
  private static String KEY_STORE_PASSWORD = "-keystorePassword";
  private static String KEY_STORE_TYPE = "-keystoreType";
  private static String KEY_PASSWORD = "-keyPassword";
  private static String IMPORT_PASSWORD = "-importPassword";

  private static List<Parameter> paramDesc = Arrays.asList(
    new Parameter[] {
      new Parameter(KEY_FILE, true, "Name of file containing a private key in PEM or DER form", null),
      new Parameter(ALIAS, true, "The alias that this key should be imported as", null),
      new Parameter(CERT_FILE, true, "Name of file containing the certificate that corresponds to the key named by '-keyFile'", null),
      new Parameter(KEY_STORE, false, "Name of the keystore to import the private key into.", "~/.keystore"),
      new Parameter(KEY_STORE_PASSWORD, false, "Keystore password", "changeit"),
      new Parameter(KEY_STORE_TYPE, false, "Type of keystore; must be JKS or PKCS12", "JKS"),
      // If this password is different than the key store password, Tomcat (at least) chokes on 
			// it with: java.security.UnrecoverableKeyException: Cannot recover key
      new Parameter(KEY_PASSWORD, false, "The password to protect the imported key with", "changeit"),
      new Parameter(IMPORT_PASSWORD, false, "The password that the imported key is encrypted with, if encrypted", "changeit")
    });

  private static void usage()  {
	  
    for (Parameter param : paramDesc)  {
      System.out.println(param.flag + "\t" + (param.required ? "required" : "optional") + "\t" +
        param.description + "\t" + 
        (param.defaultValue != null ? ("default '" + param.defaultValue + "'") : ""));
    }
    
  }

  public static void main(String[] args) throws IOException, GeneralSecurityException, KeyImportException {
	  
    Map<String, String> parsedArgs = new HashMap<String, String>();
    
    for (Parameter param : paramDesc)  {
      if (param.defaultValue != null)  {
        parsedArgs.put(param.flag, param.defaultValue);
      }
    }
    
    for (int i = 0; i < args.length; i += 2)  {
      parsedArgs.put(args[i], args[i + 1]);
    }

    boolean invalidParameters = false;
    
    for (Parameter param : paramDesc)  {
      if (param.required && parsedArgs.get(param.flag) == null)  {
        System.err.println("Missing required parameter " + param.flag);
        invalidParameters = true;
      }
    }
    
    for (String key : parsedArgs.keySet())  {
      if (!paramDesc.contains(new Parameter(key, false, null, null)))  {
        System.err.println("Invalid parameter '" + key + "'");
        invalidParameters = true;
      }
    }
    
    if (invalidParameters)  {
      usage();
      System.exit(0);
    }

    KeyStore ks = KeyStore.getInstance(parsedArgs.get(KEY_STORE_TYPE));
    InputStream keyStoreIn = new FileInputStream(parsedArgs.get(KEY_STORE));
    
    try  {
      ks.load(keyStoreIn, parsedArgs.get(KEY_STORE_PASSWORD).toCharArray());
    } finally  {
      keyStoreIn.close();
    }

    Certificate cert;
    CertificateFactory fact = CertificateFactory.getInstance("X.509");
    FileInputStream certIn = new FileInputStream(parsedArgs.get(CERT_FILE));
    
    try  {
      cert = fact.generateCertificate(certIn);
    } finally  {
      certIn.close();
    }

    PrivateKey privateKey = readPrivateKeyFile(parsedArgs.get(KEY_FILE),
      parsedArgs.get(IMPORT_PASSWORD));
    ks.setKeyEntry(parsedArgs.get(ALIAS), privateKey, 
      parsedArgs.get(KEY_PASSWORD).toCharArray(), new Certificate[] {cert});

    OutputStream keyStoreOut = new FileOutputStream(parsedArgs.get(KEY_STORE));
    
    try  {
      ks.store(keyStoreOut, parsedArgs.get(KEY_STORE_PASSWORD).toCharArray());
    } finally  {
      keyStoreOut.close();
    }
  }
  
}