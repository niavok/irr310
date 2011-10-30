package com.irr310.client.network.request;

import com.irr310.client.network.NetworkRequest;
import com.irr310.common.network.protocol.LoginRequestMessage;
import com.irr310.common.network.protocol.LoginResponseMessage;
import com.irr310.common.network.protocol.ShipListMessage;
import com.irr310.common.network.protocol.ShipListRequestMessage;

public class FetchShipListRequest extends NetworkRequest<ShipListMessage>{

	public FetchShipListRequest() {
		super(new ShipListRequestMessage(), ShipListMessage.class);
	}

	
	
}
