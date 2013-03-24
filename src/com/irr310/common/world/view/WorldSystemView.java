package com.irr310.common.world.view;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkField;
import com.irr310.common.tools.Vec2;
import com.irr310.i3d.Color;

@NetworkClass
public class WorldSystemView {

    @NetworkField
    public long id;
    
    @NetworkField
    public Vec2 location;

    @NetworkField
    public String name;

    @NetworkField
    public Long ownerId;

    @NetworkField
    public boolean homeSystem;

    @NetworkField
    public Color ownerColor;

}
