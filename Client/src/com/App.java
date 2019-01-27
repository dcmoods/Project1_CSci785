package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.Scanner;

public class App {

	private static Socket socket;
	private static String operationCommand = "";
	private static String tableFQDN = "";
	private static String keyCommand = "";
	private static String finalCommand = "";
	private static String dataCommand = "";
	private static AppClient client;
	
	public static void main(String[] args) {
		try {
	        Scanner scanner = new Scanner(System.in);
	        while (true) {	        	

        		client = new AppClient("localhost", 5555);
	            System.out.println("\nEnter a command to send to the server (empty to quit):");
	            String command = scanner.nextLine(); 
	            if (command == null || command.isEmpty()) {
	            	break;
	            }
	            
	            List<String> commands = new ArrayList<String>(Arrays.asList(command.split("\\s")));
	            
	            String response = "";
	            
	            //determine first command
	            if (commands.size() >= 1) {
	            	String operation = commands.get(0);
	            	if (operation.equalsIgnoreCase("get") || 
            			operation.equalsIgnoreCase("del") ||
            			operation.equalsIgnoreCase("put") ) {
	            			operationCommand = operation;
	            	} else {
	            		response += operation + " is not a valid command. \n";
	            	}
	            } else {
	            	response += "Command is required. \n";
	            }
	            
	            //determine table location from name server
        		if (commands.size() >= 2) {
        			String tableCommand = commands.get(1).toLowerCase();
	            	if (tableCommand.equalsIgnoreCase("phone") || 
            			tableCommand.equalsIgnoreCase("email")) {
	            		
	            		System.out.println("Sending call to client.");
	        			String tableHost = client.sendEcho(tableCommand);
	        				
	            		tableFQDN = tableHost;
	            		client.close();
	            	} else {
	            		response += tableCommand + " is not a valid table option. \n";
	            	}
	            } else {
	            	response += "Command requires a table option. \n";
	            }
        		
        		if (commands.size() >= 3) {
        			keyCommand = ":"+commands.get(2).toLowerCase();        			
        		} else {
	            	response += "Command requires a key option. \n";
	            }
        		
        		if (commands.size() >= 4 && operationCommand.equalsIgnoreCase("put")) {
        			dataCommand = ":"+commands.get(3).toLowerCase();        			
        		} else if (commands.size() >= 3 && operationCommand.equalsIgnoreCase("put")) {
	            	response += "Command requires a data option. \n";
	            }
        		
        		if (!response.isEmpty()) {
        			System.out.println(response);
        		} else {
        			finalCommand = operationCommand + keyCommand + dataCommand; 
        			String address[] = tableFQDN.split(":");
        			client = new AppClient(address[0].toString(), Integer.parseInt(address[1]));
        			String out = client.sendEcho(finalCommand);
        			System.out.println(out);
        		}
 	            
	        }

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
