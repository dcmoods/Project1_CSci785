package com.moody;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.*;
import java.util.Properties;

public class DataServer extends Thread {
	 
    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[1024];
    private InetAddress _host;
    private Properties prop;
    private byte[] in;
	private byte[] out;
 
    public DataServer(String host, int port) throws FileNotFoundException, IOException {
    	_host = InetAddress.getByName(host);
        socket = new DatagramSocket(port, _host);
        prop = new Properties();
        prop.load(new FileInputStream("config.properties")); 
    }
 
    public void run() {
        running = true;
 
        while (running) {
        	System.out.println("Email Server up!");
        	
        	in = new byte[1024];
			out = new byte[1024];
			
            DatagramPacket packet = new DatagramPacket(in, in.length);
            try {
				socket.receive(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
             
            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            String received = new String(packet.getData(), 0, packet.getLength());
            
            if (received.equals("end")) {
                running = false;
                continue;
            } 
            
            
            try {
            	String commandResponse = parseCommand(received);
            	//String data = prop.getProperty(received.toString().trim());
            	//System.out.println("Response from server: " + data);
            	out = commandResponse.getBytes();
            	packet = new DatagramPacket(out, out.length, address, port);
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        socket.close();
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
