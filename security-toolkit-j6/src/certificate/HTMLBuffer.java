package certificate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLBuffer {
	public static final String FILENAME = "CertificateDetails.html";
	public static void main(String string) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(FILENAME, true);
			bw = new BufferedWriter(fw);
			bw.write(string);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
}