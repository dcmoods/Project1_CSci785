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

public class NameServer {
	private static Properties prop;
	public static void main(String[] args) throws Exception {
        prop = new Properties();
        InputStream input = null;
        input = new FileInputStream("config.properties");
        
        try {
        	prop.load(input);        
        	System.out.println(prop.getProperty("phone"));
        	System.out.println(prop.getProperty("email"));
        } catch (IOException e) {
        	System.out.println("Error: " + e.getMessage());
        }
                
        System.out.println("The name server is running.");
        int clientNumber = 0;
        try (ServerSocket listener = new ServerSocket(5002)) {
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
                    out.println(prop.getProperty(input));
                }
            } catch (IOException e) {
                System.out.println("Error handling client #" + clientNumber);
            } finally {
                try { socket.close(); } catch (IOException e) {}
                System.out.println("Connection with client # " + clientNumber + " closed");
            }
        }
    }

}
