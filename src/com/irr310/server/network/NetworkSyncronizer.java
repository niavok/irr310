package com.irr310.server.network;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.irr310.common.network.NetworkClient;
import com.irr310.common.network.protocol.PartStateUpdateListMessage;
import com.irr310.common.world.Part;
import com.irr310.common.world.view.PartStateView;
import com.irr310.server.GameServer;

public class NetworkSyncronizer implements Runnable {

    private final ServerNetworkEngine serverNetworkEngine;

    public NetworkSyncronizer(ServerNetworkEngine serverNetworkEngine) {
        this.serverNetworkEngine = serverNetworkEngine;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            
            Map<SocketChannel, NetworkClient> clients = serverNetworkEngine.getWorker().getClients();
            
            
            
            List<PartStateView> partStateList = new ArrayList<PartStateView>();
            
            for(Part part :  GameServer.getInstance().getWorld().getParts()) {
                partStateList.add(part.toStateView());
            }
            for (NetworkClient client : clients.values()) {
                client.send(new PartStateUpdateListMessage(partStateList));
            }
        }
    }

}
