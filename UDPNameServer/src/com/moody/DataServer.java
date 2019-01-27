package com.moody;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class DataServer extends Thread {
	 
    private DatagramSocket socket;
    private boolean running;
    private InetAddress _host;
    private Properties prop;
    private PropertyParser parser;

    private byte[] in;
	private byte[] out;
 
    public DataServer(String host, int port) throws FileNotFoundException, IOException {
    	_host = InetAddress.getByName(host); //setup host
        socket = new DatagramSocket(port, _host); //setup socket
        prop = new Properties(); //init prop
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
    }
 
    public void run() {
        running = true;
 
        while (running) {
        	System.out.println("Name Server up!");
        	
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
            System.out.println("Request to server: " + received);
            
            if (received.equals("end")) {
                running = false;
                continue;
            }
            try {
            	String data = parser.getPropertyValue(received.toString().trim());
            	System.out.println("Response from server: " + data);
            	out = data.getBytes();
            	packet = new DatagramPacket(out, out.length, address, port);
				socket.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        socket.close();
    }
    
    
}

