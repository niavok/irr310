package com.irr310.server.network;

import java.nio.channels.SocketChannel;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.server.game.Player;

public class NetworkClient {
    private SocketChannel socket;
    private final NetworkEngine networkEngine;
    private final NioServer server;
    private Player player;

    
    public NetworkClient(NetworkEngine networkEngine, NioServer server, SocketChannel socket) {
        this.networkEngine = networkEngine;
        this.server = server;
        this.socket = socket;
        this.player = null;
    }

    public SocketChannel getSocket() {
        return socket;
    }

    public NetworkEngine getEngine() {
        return networkEngine;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    public Player getPlayer() {
        return player;
    }

    public boolean isLogged() {
        return player != null;
    }

    public void send(NetworkMessage message) {
        server.send(socket, message.getBytes());
    }
}
