package com.moody;

import java.io.IOException;
import java.net.Socket;

public class ServerUtils {
	

	public static boolean hostAvailabilityCheck(String host, int port) { 
	    try (Socket s = new Socket(host, port)) {
	    	s.close();
	        return true;
	    } catch (IOException ex) {
	        /* ignore */
	    }
	    return false;
	}
}
