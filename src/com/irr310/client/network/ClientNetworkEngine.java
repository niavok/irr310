package com.irr310.client.network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.irr310.common.engine.EventEngine;
import com.irr310.common.event.DefaultEngineEventVisitor;
import com.irr310.common.event.EngineEvent;
import com.irr310.common.event.NetworkEvent;
import com.irr310.common.event.QuitGameEvent;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.protocol.ShipListMessage;
import com.irr310.common.world.view.ShipView;

public class ClientNetworkEngine extends EventEngine {

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

    @Override
    protected void processEvent(EngineEvent e) {
        e.accept(new NetworkEngineEventVisitor());
    }

    private final class NetworkEngineEventVisitor extends DefaultEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping network engine");
            setRunning(false);

        }

        @Override
        public void visit(NetworkEvent event) {

            long responseIndex = event.getMessage().getResponseIndex();
            if (responseIndex != 0) {
                if (pendingRequests.containsKey(responseIndex)) {
                    NetworkRequest<?> request = pendingRequests.remove(responseIndex);
                    request.setResponse(event.getMessage());
                    return;
                }
            }

            switch (event.getMessage().getType()) {
                case SHIP_LIST:
                    shipListReceived(event.getMessage());
                    break;
                default:
                    System.err.println("Unsupported network type " + event.getMessage().getType());
            }
        }

    }

    @Override
    protected void init() {
    }

    @Override
    protected void end() {
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
