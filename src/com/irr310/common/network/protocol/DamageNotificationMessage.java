package com.irr310.common.network.protocol;

import com.irr310.common.event.DamageEvent.DamageType;
import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.world.Part;

public class DamageNotificationMessage extends NetworkMessage {

    @NetworkField
    public double damage;
    
    @NetworkField
    public long target;
    
    @NetworkField
    public  int damageType;
    
    public DamageNotificationMessage() {
        super(NetworkMessageType.DAMAGE_NOTIFICATION);
    }
    
    public DamageNotificationMessage(Part target, double damage, DamageType damageType) {
        super(NetworkMessageType.DAMAGE_NOTIFICATION);
        this.damage = damage;
        this.damageType = damageType.ordinal();
        this.target = target.getId();
    }

}
