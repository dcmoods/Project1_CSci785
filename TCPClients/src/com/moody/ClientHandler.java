package com.moody;

import java.io.*;
import java.net.*;

public class ClientHandler{
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	public void startConnection(String host, int port) throws UnknownHostException, IOException {
		clientSocket = new Socket(host, port);
		clientSocket.setReuseAddress(true);
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	}
	
	public String sendMessage(String msg) throws IOException {
		out.println(msg);
		String response = in.readLine();
		return response;
	}
	
	public void stopConnection() throws IOException {
		in.close();
		out.close();
		clientSocket.close();
	}
}
