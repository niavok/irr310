package com.irr310.client.network;

import java.net.InetAddress;


public class NetworkServer {

	private NioClient client;
	private RspHandler handler;

	public NetworkServer(String hostname, int port) {
		try {
			client = new NioClient(InetAddress.getByName(hostname), port);
			handler = new RspHandler();
			client.init(handler);
			Thread t = new Thread(client);
			t.setDaemon(true);
			t.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(NetworkRequest request) {
		//client.send("GET / HTTP/1.0\r\n\r\n".getBytes());
		//handler.waitForResponse();
		client.send("I want to login !!!".getBytes());
		
	}

	
	
	
}
