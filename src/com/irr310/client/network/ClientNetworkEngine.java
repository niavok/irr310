package com.irr310.client.network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.common.network.protocol.ShipListMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;
import com.irr310.common.world.ShipView;

public class ClientNetworkEngine {

    private NioClient client;
    private ClientNetworkWorker handler;
    private long responseIndex;
    private Map<Long, NetworkRequest<?>> pendingRequests;

    public ClientNetworkEngine(String hostname, int port) {
        try {
            responseIndex = 1;
            pendingRequests = new HashMap<Long, NetworkRequest<?>>();

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

    public void sendRequest(NetworkRequest<?> networkRequest) {

        long responseIndex = pickResponseIndex();
        networkRequest.getRequestMessage().setResponseIndex(responseIndex);
        pendingRequests.put(responseIndex, networkRequest);
        send(networkRequest.getRequestMessage());

    }

    public void pushMessage(NetworkMessage message) {

        long responseIndex = message.getResponseIndex();
        if (responseIndex != 0) {
            if (pendingRequests.containsKey(responseIndex)) {
                NetworkRequest<?> request = pendingRequests.remove(responseIndex);
                request.setResponse(message);
                return;
            }
        }

        switch (message.getType()) {
            case SHIP_LIST:
                shipListReceived(message);
                break;
            default:
                System.err.println("Unsupported network type " + message.getType());
        }

    }

    private void shipListReceived(NetworkMessage message) {
        ShipListMessage m = (ShipListMessage) message;
        System.out.println("Ship list received");
        for (ShipView ship : m.shipsList) {
            System.out.println("Ship received: " + ship.id);
            ;
        }

    }

}
