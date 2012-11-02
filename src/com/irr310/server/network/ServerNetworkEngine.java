package com.irr310.server.network;

import java.io.IOException;

import com.irr310.common.engine.EventEngine;
import com.irr310.common.event.game.DefaultGameEventVisitor;
import com.irr310.common.event.game.GameEvent;

public class ServerNetworkEngine extends EventEngine<GameEvent> {

    private NetworkWorker worker;

    public ServerNetworkEngine() {

        try {
            worker = new NetworkWorker(this);
            new Thread(worker).start();
            new Thread(new NioServer(null, 22310, worker)).start();
            NetworkSyncronizer syncronizer = new NetworkSyncronizer(this);
            syncronizer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void processEvent(GameEvent e) {
        e.accept(new NetworkEngineEventVisitor());
    }

    private final class NetworkEngineEventVisitor extends DefaultGameEventVisitor {
//        @Override
//        public void visit(QuitGameEvent event) {
//            System.out.println("stopping network engine");
//            setRunning(false);
//
//        }
//        
//        
//        @Override
//        public void visit(DamageEvent event) {
//            
//            DamageNotificationMessage damageNotificationMessage = new DamageNotificationMessage(event.getTarget(), event.getDamage(), event.getImpact());
//            
//            for(NetworkClient client: worker.getClients().values()) {
//                client.send(damageNotificationMessage);
//            }
//        }
//        
//        @Override
//        public void visit(CelestialObjectRemovedEvent event) {
//            
//            CelestialObjectRemovedNotificationMessage notificationMessage = new CelestialObjectRemovedNotificationMessage(event.getObject(), event.getReason());
//            
//            for(NetworkClient client: worker.getClients().values()) {
//                client.send(notificationMessage);
//            }
//        }
//
//        @Override
//        public void visit(NetworkEvent event) {
//            NetworkMessage message = event.getMessage();
//            switch (message.getType()) {
//                case LOGIN_REQUEST: {
//
//                    if (event.getClient().isLogged()) {
//                        event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "already logged as "
//                                + event.getClient().getPlayer().getLogin(), null));
//                        break;
//                    }
//
//                    LoginRequestMessage m = (LoginRequestMessage) message;
//                    if (!GameServer.getInstance().isPlayerExist(m.login)) {
//                        event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "unknown user", null));
//                        break;
//                    }
//
//                    Player player = GameServer.getInstance().getPlayerByLogin(m.login);
//
//                    if (!player.checkPassword(m.password)) {
//                        event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "bad password", null));
//                        break;
//                    }
//
//                    // Ok, you can login.
//
//                    event.getClient().setPlayer(player);
//
//                    event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), true, "success", player.toView()));
//                }
//                    break;
//
//                case SIGNUP_REQUEST: {
//                    if (event.getClient().isLogged()) {
//                        event.getClient().send(new SignupResponseMessage(message.getResponseIndex(), false, "already logged as "
//                                + event.getClient().getPlayer().getLogin()));
//                        break;
//                    }
//
//                    SignupRequestMessage m = (SignupRequestMessage) message;
//                    if (GameServer.getInstance().isPlayerExist(m.login)) {
//                        event.getClient().send(new SignupResponseMessage(message.getResponseIndex(), false, "username already used"));
//                        break;
//                    }
//
//                    // Ok, you can create the account
//
////                    GameServer.getInstance().createPlayer(m.login, m.password);
////                    event.getClient().send(new SignupResponseMessage(message.getResponseIndex(), true, "success"));
//                }
//                    break;
//
//                case SHIP_LIST_REQUEST: {
//                    if (!event.getClient().isLogged()) {
//                        break;
//                    }
//
//                    List<ShipView> shipList = new ArrayList<ShipView>();
//
//                    for (Ship ship : event.getClient().getPlayer().getShipList()) {
//                        shipList.add(ship.toView());
//                    }
//
//                    event.getClient().send(new ShipListMessage(message.getResponseIndex(), shipList));
//
//                }
//                    break;
//
////                case CAMERA_VIEW_OBJECT_LIST_REQUEST: {
////                    if (!event.getClient().isLogged()) {
////                        break;
////                    }
////                    World world = Game.getInstance().getWorld();
////
////                    List<CelestialObjectView> objectList = new ArrayList<CelestialObjectView>();
////
////                    for (CelestialObject object : world.getCelestialsObjects()) {
////                        objectList.add(object.toView());
////                    }
////
////                    List<ShipView> shipList = new ArrayList<ShipView>();
////
////                    for (Ship ship : world.getShips()) {
////                        shipList.add(ship.toView());
////                    }
////                    
////                    event.getClient().send(new WorldObjectListMessage(message.getResponseIndex(), objectList, shipList));
////                }
////                    break;
////
////                case CAPACITY_UPDATE: {
////                    if (!event.getClient().isLogged()) {
////                        break;
////                    }
////                    CapacityUpdateMessage m = (CapacityUpdateMessage) message;
////                    CapacityView capacityView = m.capacity;
////                    Capacity capacity = GameServer.getInstance().getWorld().getCapacityById(capacityView.id);
////
////                    capacity.fromView(capacityView);
////                }
////                    break;
////                case PART_STATE_UPDATE_LIST: {
////                    PartStateUpdateListMessage m = (PartStateUpdateListMessage) message;
////                    for (PartStateView partStateView : m.partStateList) {
////                        Part part = GameServer.getInstance().getWorld().getPartById(partStateView.id);
////                        if (part != null) {
////                            // System.out.println("update part");
////                            part.fromStateView(partStateView);
////                        }
////                    }
////                    GameServer.getInstance().getPhysicEngine().reloadStates();
////                    break;
////                }
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

    public NetworkWorker getWorker() {
        return worker;
    };

}
