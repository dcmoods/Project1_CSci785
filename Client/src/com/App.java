package com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.Scanner;

public class App {

	private static Socket socket;
	private static String operationCommand = "";
	private static String tableLocation = "";
	private static String keyCommand = "";
	private static String finalCommand = "";
	
	public static void main(String[] args) throws Exception {
	        System.out.println("Enter the IP address of a machine running the name server:");
	        String serverAddress = new Scanner(System.in).nextLine();
	        socket = new Socket(serverAddress, 5002);

	        // Streams for conversing with server
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

	        // Consume and display welcome message from the server
	        System.out.println(in.readLine());

	        Scanner scanner = new Scanner(System.in);
	        while (true) {	        	
	            System.out.println("\nEnter a command to send to the server (empty to quit):");
	            String command = scanner.nextLine(); 
	            if (command == null || command.isEmpty()) {
	            	break;
	            }
	            
	            List<String> commands = new ArrayList<String>(Arrays.asList(command.split("\\s")));
	            
	            String response = "";
	            
	            //determine first command
	            if (0 < commands.size()) {
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
        		if (1 < commands.size()) {
        			String tableCommand = commands.get(1).toLowerCase();
	            	if (tableCommand.equalsIgnoreCase("phone") || 
            			tableCommand.equalsIgnoreCase("email")) {
	            		out.println(tableCommand);
	            		tableLocation = in.readLine();
	            	} else {
	            		response += tableCommand + " is not a valid table option. \n";
	            	}
	            } else {
	            	response += "Command requires a table option. \n";
	            }
        		
        		if (2 < commands.size()) {
        			keyCommand = commands.get(2).toLowerCase();        			
        		} else {
	            	response += "Command requires a key option. \n";
	            }
        		
        		if (!response.isEmpty()) {
        			System.out.println(response);
        		} else {
        			finalCommand = operationCommand + " " + tableLocation + " " + keyCommand; 
        			System.out.println(finalCommand);        		
        		}
 	            
	        }
	    }

}
