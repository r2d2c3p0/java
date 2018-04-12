package org.r2d2c3p0.simpletext;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetHostName {

    public static String run() {
        InetAddress iAddress;
        String hostName=null;
		try {
			iAddress = InetAddress.getLocalHost();
			hostName = iAddress.getHostName();
			boolean isEmpty = hostName == null || hostName.trim().length() == 0;
			if (isEmpty) {
				String canonicalHostName = iAddress.getCanonicalHostName();
				return canonicalHostName;
			}			
			return hostName;	        
		} catch (UnknownHostException e) {
			return "Unknown host";
		}
    }
}