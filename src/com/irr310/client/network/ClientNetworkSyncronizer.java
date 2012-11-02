package com.irr310.client.network;


public class ClientNetworkSyncronizer extends Thread{

    private final ClientNetworkEngine clientNetworkEngine;

    public ClientNetworkSyncronizer(ClientNetworkEngine clientNetworkEngine) {
        this.clientNetworkEngine = clientNetworkEngine;
        setDaemon(true);
    }

    @Override
    public void run() {
        
        
//        while(true) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//            }
//            
//            List<PartStateView> partStateList = new ArrayList<PartStateView>();
//            
//            for(Part part :  Game.getInstance().getWorld().getMyParts()) {
//                partStateList.add(part.toStateView());
//            }
//            
//            if(partStateList.size() > 0) {
//                clientNetworkEngine.send(new PartStateUpdateListMessage(partStateList));
//            }
//        }
    }

}
