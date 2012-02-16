package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkMessage;
import com.irr310.common.network.NetworkField;

public class CameraViewObjectListRequestMessage extends NetworkMessage {

    public CameraViewObjectListRequestMessage() {
        super(NetworkMessageType.CAMERA_VIEW_OBJECT_LIST_REQUEST);
    }
    
}
