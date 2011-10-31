package com.irr310.common.network;

import java.nio.channels.SocketChannel;

import com.irr310.common.world.Player;
import com.irr310.server.network.ServerNetworkEngine;

public class NetworkClient {
    private SocketChannel socket;
    private final ServerNetworkEngine networkEngine;
    private final NioPeer peer;
    private Player player;

    
    public NetworkClient(ServerNetworkEngine networkEngine, NioPeer peer, SocketChannel socket) {
        this.networkEngine = networkEngine;
        this.peer = peer;
        this.socket = socket;
        this.player = null;
    }

    public SocketChannel getSocket() {
        return socket;
    }

    public ServerNetworkEngine getEngine() {
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
        peer.send(socket, message.getBytes());
    }
}
