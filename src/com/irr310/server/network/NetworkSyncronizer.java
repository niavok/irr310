package com.irr310.server.network;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.irr310.common.network.NetworkClient;
import com.irr310.common.network.protocol.PartStateUpdateListMessage;
import com.irr310.common.world.view.PartStateView;
import com.irr310.common.world.zone.Part;
import com.irr310.server.GameServer;

public class NetworkSyncronizer extends Thread {

    private final ServerNetworkEngine serverNetworkEngine;

    public NetworkSyncronizer(ServerNetworkEngine serverNetworkEngine) {
        this.serverNetworkEngine = serverNetworkEngine;
        setDaemon(true);
    }

    @Override
    public void run() {

        while (true) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }

            Map<SocketChannel, NetworkClient> clients = serverNetworkEngine.getWorker().getClients();

            for (NetworkClient client : clients.values()) {
                List<PartStateView> partStateList = new ArrayList<PartStateView>();

                for (Part part : GameServer.getInstance().getWorld().getParts()) {
                    if (part.getOwner() != client.getPlayer()) {
                        partStateList.add(part.toStateView());
                    }
                }
                if (partStateList.size() > 0) {
                   // System.err.println("send "+ partStateList.size()+" updates");
                    client.send(new PartStateUpdateListMessage(partStateList));
                }
            }
        }
    }

}
