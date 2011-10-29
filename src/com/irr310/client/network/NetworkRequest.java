package com.irr310.client.network;

import com.irr310.common.network.protocol.NetworkMessage;

public class NetworkRequest {

	boolean hasResponse;
	private final NetworkMessage requestMessage;
	private final NetworkMessage responseMessage;

	public NetworkRequest(NetworkMessage requestMessage,
			NetworkMessage responseMessage) {
		this.requestMessage = requestMessage;
		this.responseMessage = responseMessage;
		hasResponse = false;
	}
	
	public void sendAndWait(NetworkServer network) {
		network.sendRequest(requestMessage, responseMessage);
	}

	public synchronized void waitForResponse() {
		while (!hasResponse) {
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
		}
	}

}
