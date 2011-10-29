package com.irr310.client.network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.irr310.common.network.protocol.NetworkMessage;

public class NetworkServer {

    private NioClient client;
    private RspHandler handler;
    private long responseIndex;
    private Map<Long,NetworkMessage> exceptedResponses;

    public NetworkServer(String hostname, int port) {
        try {
            responseIndex = 1;
            exceptedResponses= new HashMap<Long,NetworkMessage>();
            
            
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

    private synchronized long pickResponseIndex() {
        return responseIndex++;
    }

    public void send(NetworkMessage message) {
        client.send(message.getBytes());
    }

    public void sendRequest(NetworkMessage requestMessage, NetworkMessage responseMessage) {
        long responseIndex = pickResponseIndex();
        requestMessage.setResponseIndex(responseIndex);
        exceptedResponses.put(responseIndex, responseMessage);

        send(requestMessage);
        
    }

}
