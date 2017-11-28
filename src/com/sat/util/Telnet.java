package com.sat.util;

import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Telnet {
	
	@SuppressWarnings("unused")
	private static int SUCCESS = 0;
	// Telnet for the servers where original 'telnet' is not installed.
	
	public static void TelnetMain(String hostname, String Port) {
		
		int port = Integer.parseInt(Port);
		SocketAddress sockaddr = new InetSocketAddress(hostname, port);
		Socket socket = new Socket();
		// Connect with 2 s timeout.
		try {
			System.out.println("Connecting to "+hostname+" at "+port+"...");
		    socket.connect(sockaddr, 2000);
		    try {
		        socket.close();
		        System.out.println("Connection closed successfully: "+hostname+" at "+port);
		        SUCCESS = 1;
		    } catch (IOException ex) {
		        System.out.println("Error occured trying to close the socket for "+hostname+" at "+port);
		    }
		} catch (SocketTimeoutException stex) {
			System.out.println("Socket timed out for "+hostname+" at "+port);
		} catch (IOException iOException) {
			System.out.println("Error(IOE) occured for "+hostname+" at "+port);
		}
	}

}