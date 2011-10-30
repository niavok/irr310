package com.irr310.server.network;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.irr310.common.engine.EventEngine;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.protocol.LoginRequestMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.common.network.protocol.ShipListMessage;
import com.irr310.common.network.protocol.SignupRequestMessage;
import com.irr310.common.network.protocol.SignupResponseMessage;
import com.irr310.common.world.ShipView;
import com.irr310.server.GameServer;
import com.irr310.server.event.DefaultServerEngineEventVisitor;
import com.irr310.server.event.NetworkEvent;
import com.irr310.server.event.QuitGameEvent;
import com.irr310.server.event.ServerEngineEvent;
import com.irr310.server.game.Player;
import com.irr310.server.game.world.Ship;

public class NetworkEngine extends EventEngine<ServerEngineEvent> {

    public NetworkEngine() {

        try {
            NetworkWorker worker = new NetworkWorker(this);
            new Thread(worker).start();
            new Thread(new NioServer(null, 22310, worker)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void processEvent(ServerEngineEvent e) {
        e.accept(new NetworkEngineEventVisitor());
    }

    private final class NetworkEngineEventVisitor extends DefaultServerEngineEventVisitor {
        @Override
        public void visit(QuitGameEvent event) {
            System.out.println("stopping network engine");
            setRunning(false);

        }

        @Override
        public void visit(NetworkEvent event) {
            NetworkMessage message = event.getMessage();
            switch (message.getType()) {
                case LOGIN_REQUEST: {

                    if (event.getClient().isLogged()) {
                        event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "already logged as "
                                + event.getClient().getPlayer().getLogin()));
                        break;
                    }

                    LoginRequestMessage m = (LoginRequestMessage) message;
                    if (!GameServer.getInstance().getGame().isPlayerExist(m.login)) {
                        event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "unknown user"));
                        break;
                    }

                    Player player = GameServer.getInstance().getGame().getPlayerByLogin(m.login);

                    if (!player.checkPassword(m.password)) {
                        event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), false, "bad password"));
                        break;
                    }

                    // Ok, you can login.

                    event.getClient().setPlayer(player);

                    event.getClient().send(new LoginResponseMessage(message.getResponseIndex(), true, "success"));
                }
                    break;

                case SIGNUP_REQUEST: {
                    if (event.getClient().isLogged()) {
                        event.getClient().send(new SignupResponseMessage(message.getResponseIndex(), false, "already logged as "
                                + event.getClient().getPlayer().getLogin()));
                        break;
                    }

                    SignupRequestMessage m = (SignupRequestMessage) message;
                    if (GameServer.getInstance().getGame().isPlayerExist(m.login)) {
                        event.getClient().send(new SignupResponseMessage(message.getResponseIndex(), false, "username already used"));
                        break;
                    }
                    
                    // Ok, you can create the account
                    
                    GameServer.getInstance().getGame().createPlayer(m.login, m.password);
                    event.getClient().send(new SignupResponseMessage(message.getResponseIndex(), true, "success"));
                }
                    break;
                    
                case SHIP_LIST_REQUEST: {
                    if (!event.getClient().isLogged()) {
                        break;
                    }
                    
                    List<ShipView> shipList = new ArrayList<ShipView>();
                    
                    for(Ship ship : event.getClient().getPlayer().getShipList()) {
                        shipList.add(ship.toView());
                    }
                    
                    event.getClient().send(new ShipListMessage(message.getResponseIndex(), shipList));
                    
                    
                }
                    break;
                default:
                    System.err.println("Unsupported network type");
            }
        }

    }

    @Override
    protected void init() {
    }

    @Override
    protected void end() {
    }

    public void pushMessage(SocketChannel socketChannel, NetworkMessage message) {
        System.out.println("Network engine receive a message");

        // TODO: make a asyncronus queue

        switch (message.getType()) {
            case LOGIN_REQUEST:
                LoginRequestMessage m = (LoginRequestMessage) message;
                System.out.println("Login request: login=" + m.login + ", password=" + m.password);
                break;
        }
    }

}
