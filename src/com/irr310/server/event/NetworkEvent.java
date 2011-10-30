package com.irr310.server.event;

import com.irr310.common.network.NetworkMessage;
import com.irr310.server.network.NetworkClient;

public class NetworkEvent extends ServerEngineEvent {

    private final NetworkMessage message;
    private final NetworkClient client;

    public NetworkEvent(NetworkMessage message, NetworkClient client) {
        this.message = message;
        this.client = client;
    }

    @Override
    public void accept(ServerEngineEventVisitor visitor) {
        visitor.visit(this);
    }

    public NetworkMessage getMessage() {
        return message;
    }

    public NetworkClient getClient() {
        return client;
    }
}
