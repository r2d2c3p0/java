package org.r2d2c3p0.server.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class ListenPort {

	public static void main(String[] args) {
		String host_name=args[0];
		String port_number=args[1];
		// Creates a socket address from a host name and a port number.
		try {
			SocketAddress socketAddress = new InetSocketAddress(host_name, Integer.parseInt(port_number));
			Socket socket = new Socket();
			// Timeout in milliseconds
			int timeout = 1000;
			try {
				socket.connect(socketAddress, timeout);
				socket.close();
				System.out.println(host_name + " at " + port_number + ": UP");
			} catch (SocketTimeoutException ste) {
				System.out.println(host_name + " at " + port_number + ": DOWN");
			} catch (IOException ioe) {
				System.out.println(host_name + " at " + port_number + ": DOWN");
			}
		} catch (IllegalArgumentException iae) {
			System.out.println(host_name + " at " + port_number + ": STDIN ERR");
		}
	}

}
