package com.irr310.common.network.protocol;

import com.irr310.common.network.NetworkField;
import com.irr310.common.network.NetworkMessage;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.DamageDescriptor;
import com.irr310.common.world.Part;

public class DamageNotificationMessage extends NetworkMessage {

    @NetworkField
    public double damage;
    
    @NetworkField
    public long target;
    
    @NetworkField
    public  int damageType;
    
    @NetworkField
    public  Vec3 impact;
    
    public DamageNotificationMessage() {
        super(NetworkMessageType.DAMAGE_NOTIFICATION);
    }
    
    public DamageNotificationMessage(Part target, DamageDescriptor damage, Vec3 impact) {
        super(NetworkMessageType.DAMAGE_NOTIFICATION);
        this.damage = damage.getEffectiveDamage();
        this.damageType = damage.type.ordinal();
        this.target = target.getId();
        this.impact = impact;
    }

}
