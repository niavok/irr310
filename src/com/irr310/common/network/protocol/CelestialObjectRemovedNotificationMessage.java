package com.irr310.common.network.protocol;

import com.irr310.common.event.CelestialObjectRemovedEvent.Reason;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.world.zone.CelestialObject;

public class CelestialObjectRemovedNotificationMessage extends NetworkMessage {
    
    @NetworkField
    public long target;
    
    @NetworkField
    public  int reason;
    
    public CelestialObjectRemovedNotificationMessage() {
        super(NetworkMessageType.CELESTIAL_OBJECT_REMOVED_NOTIFICATION);
    }
    
    public CelestialObjectRemovedNotificationMessage(CelestialObject target, Reason reason) {
        super(NetworkMessageType.CELESTIAL_OBJECT_REMOVED_NOTIFICATION);
        this.reason = reason.ordinal();
        this.target = target.getId();
    }

}
