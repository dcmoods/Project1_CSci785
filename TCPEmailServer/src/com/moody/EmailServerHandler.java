package com.moody;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Set;


public class EmailServerHandler {

    private ServerSocket serverSocket;
	private static Properties prop;
	private PropertyParser parser;
 
    public void start(int port) {
        try {
			serverSocket = new ServerSocket(port);
			System.out.println("Email Server at port: " + port + " is up!");
			prop = new Properties(); 
	        prop.load(new FileInputStream("config.properties")); //load properties file
	        parser = new PropertyParser(prop); //load prop into parser
	        Set<Object> keys = parser.getAllKeys();
	    
			
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
                out.println(parseCommand(inputLine));
            }
 
            in.close();
            out.close();
            clientSocket.close();
        	} catch (Exception ex) {
        		
        	}
        }
        
        public String parseCommand(String input) {
        	String data[] = input.trim().split(":");

        	if (data.length == 3) {
        		if (data[0].equalsIgnoreCase("put") && !data[1].isEmpty() && !data[2].isEmpty()) {
        			prop.put(data[1], data[2]);
        			return "Put completed";
        		}
        	} else if (data.length == 2) {
        		if (data[0].equalsIgnoreCase("get") && !data[1].isEmpty()) {
        			String response = prop.getProperty(data[1]);
        			if (response != null) {
        				return response;
        			} else return "Unable to find that entry.";
        		}
        		
        		if (data[0].equalsIgnoreCase("del") && !data[1].isEmpty()) {
        			Object response = prop.remove(data[1]);
        			if (response != null) {
        				return (String) response + " has been removed";
        			} else return "Unable to find that entry.";
        		}        		
        	}
        		
        	return "Error: Incorrect command format.";
        }

    }
}