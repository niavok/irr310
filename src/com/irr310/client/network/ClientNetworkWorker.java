package com.irr310.client.network;

import java.util.LinkedList;
import java.util.List;

import com.irr310.common.event.NetworkEvent;
import com.irr310.common.network.MessageParser;
import com.irr310.common.network.NetworkMessage;

public class ClientNetworkWorker implements Runnable {
    private List<byte[]> queue = new LinkedList<byte[]>();
    private MessageParser messageParser;

    public ClientNetworkWorker(final ClientNetworkEngine engine) {
        messageParser = new MessageParser() {

            @Override
            public void processMessage(NetworkMessage message) {
                engine.pushEvent(new NetworkEvent(message, null));
            }
        };

    }

    public void processData(byte[] data, int count) {
        byte[] dataCopy = new byte[count];
        System.arraycopy(data, 0, dataCopy, 0, count);
        synchronized (queue) {
            queue.add(dataCopy);
            queue.notify();
        }
    }

    public void run() {
        byte[] data;

        while (true) {
            // Wait for data to become available
            synchronized (queue) {
                while (queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                    }
                }
                data = queue.remove(0);
            }
            messageParser.parseData(data);
        }
    }

}
