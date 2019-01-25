package com;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class PhoneServer {
	private static Properties prop;
	private static InputStream input = null;
	public static void main(String[] args) throws Exception {
        prop = new Properties();
        input = new FileInputStream("config.properties");
        
        try {
        	prop.load(input);                	
        } catch (IOException e) {
        	System.out.println("Error: " + e.getMessage());
        }
                
        System.out.println("The phone server is running.");
        int clientNumber = 0;
        try (ServerSocket listener = new ServerSocket(5555)) {
            while (System.in.available() == 0) {
                new Dispatcher(listener.accept(), clientNumber++).start();
            }
        }
    }

    private static class Dispatcher extends Thread {
        private Socket socket;
        private int clientNumber;

        public Dispatcher(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New client #" + clientNumber + " connected at " + socket);
        }
     
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                out.println("Hello, you are client #" + clientNumber);

           
                while (true) {
                    String input = in.readLine();
                    if (input == null || input.isEmpty()) {
                        break;
                    }
                    
                    out.println(input);
                }
            } catch (IOException e) {
                System.out.println("Error handling client #" + clientNumber);
                System.out.println(e.getMessage());
            } finally {
                try { socket.close(); } catch (IOException e) {}
                System.out.println("Connection with client # " + clientNumber + " closed");
            }
        }
        
        public String parseCommand(String input) {
        	String data[] = input.split(",");
        	System.out.println(data[0]);
        	System.out.println(data[1]);
        	if (3 >= data.length) {
        		if (data[0].equalsIgnoreCase("put") && !data[1].isEmpty() && !data[2].isEmpty()) {
        			prop.put(data[1], data[2]);
        			return "Put completed";
        		}
        	} else if (2 >= data.length) {
        		if (data[0].equalsIgnoreCase("get") && !data[1].isEmpty()) {
        			return prop.getProperty(data[1]);
        		}
        		
        		if (data[0].equalsIgnoreCase("del") && !data[1].isEmpty()) {
        			prop.remove(data[1]);
        			return "Delete completed";
        		}        		
        	}
        		
        	return "Error: Incorrect command format.";
        }
    }   
}
