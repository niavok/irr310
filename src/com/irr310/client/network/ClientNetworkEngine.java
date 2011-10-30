package com.irr310.client.network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.protocol.LoginRequestMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.server.GameServer;
import com.irr310.server.game.Player;

public class ClientNetworkEngine {

    private NioClient client;
    private ClientNetworkWorker handler;
    private long responseIndex;
    private Map<Long,NetworkMessage> exceptedResponses;

    public ClientNetworkEngine(String hostname, int port) {
        try {
            responseIndex = 1;
            exceptedResponses= new HashMap<Long,NetworkMessage>();
            
            
            client = new NioClient(InetAddress.getByName(hostname), port);
            handler = new ClientNetworkWorker(this);
            
            client.init(handler);
            Thread t = new Thread(handler);
            Thread t2 = new Thread(client);
            t.setDaemon(true);
            t.start();
            t2.setDaemon(true);
            t2.start();

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

    public void pushMessage(NetworkMessage message) {
        switch(message.getType()) {
            case LOGIN_RESPONSE:
                LoginResponseMessage m = (LoginResponseMessage) message;
                if(m.success) {
                    System.out.println("login successful");
                } else {
                    System.out.println("login failed: "+m.reason);
                }
                break;
            default:
                System.err.println("Unsupported network type");
        }
    }

}
