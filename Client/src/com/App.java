package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Scanner;

public class App {

	private static Socket socket;	
	private static String operationCommand = "";
	private static String tableFQDN = "";
	private static String keyCommand = "";
	private static String finalCommand = "";
	private static String dataCommand = "";
	
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
	            		out.println(tableCommand);
	            		tableFQDN = in.readLine();
	            	} else {
	            		response += tableCommand + " is not a valid table option. \n";
	            	}
	            } else {
	            	response += "Command requires a table option. \n";
	            }
        		
        		if (commands.size() >= 3) {
        			keyCommand = ","+commands.get(2).toLowerCase();        			
        		} else {
	            	response += "Command requires a key option. \n";
	            }
        		
        		if (commands.size() >= 4 && operationCommand.equalsIgnoreCase("put")) {
        			dataCommand = ","+commands.get(3).toLowerCase();        			
        		} else if (commands.size() >= 3 && operationCommand.equalsIgnoreCase("put")) {
	            	response += "Command requires a data option. \n";
	            }
        		
        		if (!response.isEmpty()) {
        			System.out.println(response);
        		} else {
        			finalCommand = operationCommand + keyCommand + dataCommand; 
        			String address[] = tableFQDN.split(":");
        			/*System.out.println(address[0]);
        			System.out.println(address[1]);*/
        			/*socket.close();
        			socket = new Socket(address[0], Integer.parseInt(address[1]));
        			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	        out = new PrintWriter(socket.getOutputStream(), true);
        	        System.out.println(finalCommand);
        	        out.print(finalCommand);
        			System.out.println(in);
        			socket.close();*/
        			Connection temp = new Connection(address[0], Integer.parseInt(address[1]));
        			temp.start();
        			temp.sendMessage(finalCommand);
        			System.out.println(temp.getMessage());
        			temp.terminate();        			
        		}
 	            
	        }
	    }


}
