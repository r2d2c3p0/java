package custom.ssl.tool;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Iterator;

public class GetCASigners {
	
  public static void GetCASignersMain(String Keystore, String Password) throws Exception {

    FileInputStream is = new FileInputStream(Keystore);
    KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
    keystore.load(is, Password.toCharArray());

    PKIXParameters params = new PKIXParameters(keystore);

    Iterator<TrustAnchor> it = params.getTrustAnchors().iterator();
    System.out.println("\tKeystore "+Keystore+" CA Signers Information:");
    for (int i=1; it.hasNext();i++) {
      TrustAnchor ta = (TrustAnchor) it.next();

      X509Certificate cert = ta.getTrustedCert();
      System.out.println("\t["+i+"]"+cert.getSubjectDN());
    }
  }
  
}