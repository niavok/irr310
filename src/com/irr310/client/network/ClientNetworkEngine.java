package com.irr310.client.network;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.irr310.common.engine.EventEngine;
import com.irr310.common.event.game.DefaultGameEventVisitor;
import com.irr310.common.event.game.GameEvent;
import com.irr310.common.event.game.QuitGameEvent;
import com.irr310.common.network.NetworkMessage;

public class ClientNetworkEngine extends EventEngine<GameEvent> {

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
            ClientNetworkSyncronizer syncronizer = new ClientNetworkSyncronizer(this);
            syncronizer.start();
            
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
    protected void processEvent(GameEvent e) {
        e.accept(new NetworkEngineEventVisitor());
    }

    private final class NetworkEngineEventVisitor extends DefaultGameEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping network engine");
            setRunning(false);

        }

//        @Override
//        public void visit(NetworkEvent event) {
//
//            long responseIndex = event.getMessage().getResponseIndex();
//            if (responseIndex != 0) {
//                if (pendingRequests.containsKey(responseIndex)) {
//                    NetworkRequest<?> request = pendingRequests.remove(responseIndex);
//                    request.setResponse(event.getMessage());
//                    return;
//                }
//            }
//
//            switch (event.getMessage().getType()) {
//                case SHIP_LIST:
//                    shipListReceived(event.getMessage());
//                    break;
//                case WORLD_OBJECT_LIST:
//                    worldObjectListReceived(event.getMessage());
//                    break;
//                case PART_STATE_UPDATE_LIST:
//                    partStateUpdateReceived(event.getMessage());
//                    break;
//                case DAMAGE_NOTIFICATION:
//                    damageNotificationReceived((DamageNotificationMessage) event.getMessage());
//                    break;
//                case CELESTIAL_OBJECT_REMOVED_NOTIFICATION:
//                    celestialObjectRemovedNotificationReceived((CelestialObjectRemovedNotificationMessage) event.getMessage());
//                    break;
//                default:
//                    System.err.println("Unsupported network type " + event.getMessage().getType());
//            }
//        }

        

        

    }

    @Override
    protected void init() {
    }

    @Override
    protected void end() {
    }

//    private void shipListReceived(NetworkMessage message) {
////        ShipListMessage m = (ShipListMessage) message;
////        System.out.println("Ship list received");
////        for (ShipView shipView : m.shipsList) {
////            Ship ship = GameClient.getInstance().getWorld().loadShip(shipView);
////            System.out.println("Ship received: " + ship.getId());
////        }
//
//    }
//    
//    private void celestialObjectRemovedNotificationReceived(CelestialObjectRemovedNotificationMessage message) {
////        CelestialObject celestialObject = GameClient.getInstance().getWorld().getCelestialObjectById(message.target);
////        if(celestialObject == null) {
////            return;
////        }
////        
////        GameClient.getInstance().getWorld().removeCelestialObject(celestialObject, Reason.values()[message.reason]);
//    }
//    
//    private void damageNotificationReceived(DamageNotificationMessage message) {
////        Part partById = GameClient.getInstance().getWorld().getPartById(message.target);
////        if(partById == null) {
////            return;
////        }
////        WorldObject target = partById.getParentObject();
////        
////        System.out.println("Damage from server: "+target.getName()+" take "+message.damage+" damage.");
////        double newDurablility = target.getDurability();
////        newDurablility -= message.damage;
////        if(newDurablility < 0) {
////            newDurablility = 0;
////        }
////        
////        System.out.println("new state: "+newDurablility+"/"+target.getDurabilityMax());
////        
////        target.setDurability(newDurablility);
////        
////        //TODO: set right armor penetration
////        DamageDescriptor damageDescriptor = new DamageDescriptor(DamageDescriptor.DamageType.values()[message.damageType], 0, DamageDescriptor.DamageCause.values()[message.damageCause]);
////        damageDescriptor.setEffectiveDamage(message.damage);
////        
////        Game.getInstance().sendToAll(new DamageEvent(partById, damageDescriptor, message.impact));
////        
//        
//    }
//
//    private void worldObjectListReceived(NetworkMessage message) {
////        WorldObjectListMessage m = (WorldObjectListMessage) message;
////        System.out.println("World object list received");
////        
////        for (CelestialObjectView celestialObjectView : m.celestialObjectList) {
////            CelestialObject object = GameClient.getInstance().getWorld().loadCelestialObject(celestialObjectView);
////            System.out.println("Celestial object received: " + object.getId());
////        }
////        
////        for (ShipView shipView : m.shipsList) {
////            Ship ship = GameClient.getInstance().getWorld().loadShip(shipView);
////            System.out.println("Ship received: " + ship.getId());
////        }
//
//    }
//    
//    private void partStateUpdateReceived(NetworkMessage message) {
////        PartStateUpdateListMessage m = (PartStateUpdateListMessage) message;
////        Game.getInstance().getWorld().lock();
////        for (PartStateView partStateView : m.partStateList) {
////            Part part = GameClient.getInstance().getWorld().getPartById(partStateView.id);
////            if (part != null) {
//////                System.out.println("update part: "+part.getParentObject().getName());
////                part.fromStateView(partStateView);
////            }
////        }
////        Game.getInstance().getWorld().unlock();
////        GameClient.getInstance().getPhysicEngine().reloadStates();
////        for (PartStateView partStateView : m.partStateList) {
////            Part part = GameClient.getInstance().getWorld().getPartById(partStateView.id);
////            if (part != null) {
////                System.err.println("update report: "+part.getTransform().getTranslation()+" asked "+partStateView.transform.getTranslation());
////            }
////        }
//        
//    }
}
