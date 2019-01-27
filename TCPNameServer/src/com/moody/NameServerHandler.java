package com.moody;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Set;


public class NameServerHandler {

    private ServerSocket serverSocket;
    private Properties prop;
    private static PropertyParser parser;
    
    public void start(int port) {
        try {
			serverSocket = new ServerSocket(port);
			System.out.println("Name Server at port: " + port + " is up!");
			
			prop = new Properties(); 
	        prop.load(new FileInputStream("config.properties")); //load properties file
	        parser = new PropertyParser(prop); //load prop into parser
	        Set<Object> keys = parser.getAllKeys();
	        for(Object k:keys){
	            String key = (String)k;
	            String value[] = parser.getPropertyValue(key).split(":");
	            boolean available 
	            	= ServerUtils.hostAvailabilityCheck(value[0], Integer.parseInt(value[1]));
	            
	            if(available) {
	            	System.out.println("Server - " +key+ 
	            			" at " + parser.getPropertyValue(key) + " is avalabile.");
	            } else {
	            	System.out.println("Server - " +key+ 
	            			" at " + parser.getPropertyValue(key) + " is down.");
	            }
	        }
	        //start to listen
	        while (true)
	        	new EchoClientHandler(serverSocket.accept()).start();
	        
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
 
    public void stop() throws IOException {
        serverSocket.close();
    }
 
    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
 
        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
 
        public void run() {
        	try	{
	            out = new PrintWriter(clientSocket.getOutputStream(), true);
	            in = new BufferedReader(
	              new InputStreamReader(clientSocket.getInputStream()));
	             
	            String inputLine;
	            while ((inputLine = in.readLine()) != null) {
	                if (".".equals(inputLine)) {
	                    out.println("bye");
	                    break;
	                }
	                String tableHost = parser.getPropertyValue(inputLine);
	                String address[] = tableHost.split(":");
	                if (ServerUtils.hostAvailabilityCheck(address[0], Integer.parseInt(address[1]))) {	                	
	                	out.println(tableHost);
	                } else {
	                	out.println("The table requested is not currently up.");
	                }
	            }
 
	            in.close();
	            out.close();
	            clientSocket.close();
        	} catch (Exception ex) {
        		
        	}
        }

    }
}