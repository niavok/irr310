package com.irr310.server.network;


public class NetworkSyncronizer extends Thread {

    private final ServerNetworkEngine serverNetworkEngine;

    public NetworkSyncronizer(ServerNetworkEngine serverNetworkEngine) {
        this.serverNetworkEngine = serverNetworkEngine;
        setDaemon(true);
    }

    @Override
    public void run() {

//        while (true) {
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//            }
//
//            Map<SocketChannel, NetworkClient> clients = serverNetworkEngine.getWorker().getClients();
//
//            for (NetworkClient client : clients.values()) {
//                List<PartStateView> partStateList = new ArrayList<PartStateView>();
//
//                for (Part part : GameServer.getInstance().getWorld().getParts()) {
//                    if (part.getOwner() != client.getPlayer()) {
//                        partStateList.add(part.toStateView());
//                    }
//                }
//                if (partStateList.size() > 0) {
//                   // System.err.println("send "+ partStateList.size()+" updates");
//                    client.send(new PartStateUpdateListMessage(partStateList));
//                }
//            }
//        }
    }

}
