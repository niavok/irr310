package com.irr310.server.ai;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.system.Ship;

public interface ShipDriver {

    void init(Ship ship);
    
    void setLinearVelocityCommand(Vec3 linearVelocity);
    
    void setAngularVelocityCommand(Vec3 angularVelocity);
    
    void update(float seconds);
    
}
