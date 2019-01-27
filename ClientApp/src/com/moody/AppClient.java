package com.moody;

import java.io.IOException;
import java.net.*;

public class AppClient {
    private DatagramSocket _socket;
    private InetAddress _address;
    private int _port;
 
    private byte[] buf;
    private byte[] in;
 
    public AppClient(String address, int port) throws SocketException, UnknownHostException {
        _socket = new DatagramSocket();
        _address = InetAddress.getByName(address);
        _port = port;
    }
 
    public String sendEcho(String msg) {
        buf = msg.getBytes();
        in = new byte[256];
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, _address, _port);
        try {
			_socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
        packet = new DatagramPacket(in, in.length);
        try {
			_socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
        String received = new String(
          packet.getData(), 0, packet.getLength());
        return received;
    }
 
    public void close() {
        _socket.close();
    }
}