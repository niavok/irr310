package com.irr310.common.network;

import java.nio.channels.SocketChannel;

public abstract class NioPeer {

    public abstract void send(SocketChannel socket, byte[] bytes);

}
