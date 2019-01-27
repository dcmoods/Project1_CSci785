package com.moody;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Startup {

	private static ClientHandler client;
	private static String operationCommand;
	private static String tableFQDN;
	private static String keyCommand;
	private static String dataCommand;
	private static String finalCommand;

	public static void main(String[] args) {

	    
	    try {
	        Scanner scanner = new Scanner(System.in);
	        while (true) {	        	

        		client = new ClientHandler();
        		client.startConnection("localhost", 5555);
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
	        			String nsResponse = client.sendMessage(tableCommand);
	        			if (nsResponse.contains(":")) {
	        				tableFQDN = nsResponse;
	        			} else {
	        				System.out.println(nsResponse);
	        				continue;
	        			}
	        			client.stopConnection();
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
	            } else {
	            	dataCommand = "";
	            }
        		
        		if (!response.isEmpty()) {
        			System.out.println(response);
        		} else {
        			finalCommand = operationCommand + keyCommand + dataCommand; 
        			String address[] = tableFQDN.split(":");
        			client = new ClientHandler();
        			client.startConnection(address[0].toString(), Integer.parseInt(address[1]));
        			String out = client.sendMessage(finalCommand);
        			System.out.println(out);
        		}
 	            
	        }
        	client.stopConnection();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
