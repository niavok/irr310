package com.irr310.client.network;

import com.irr310.common.network.NetworkMessage;

public class NetworkRequest<T> {

    boolean hasResponse;
    private final NetworkMessage requestMessage;
    private T responseMessage;
    private final Class<T> reponseType;

    public NetworkRequest(NetworkMessage requestMessage, Class<T> reponseType) {
        this.requestMessage = requestMessage;
        this.responseMessage = null;
        this.reponseType = reponseType;
    }

    public void sendAndWait(ClientNetworkEngine network) {
        network.sendRequest(this);
        waitForResponse();
    }

    public synchronized void waitForResponse() {
        while (responseMessage == null) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public NetworkMessage getRequestMessage() {
        return requestMessage;
    }

    public T getResponseMessage() {
        return responseMessage;
    }

    public void setResponse(NetworkMessage message) {
        synchronized (this) {
            responseMessage = (T) message;
            this.notify();
        }
    }
}
