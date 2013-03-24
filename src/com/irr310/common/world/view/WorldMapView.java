package com.irr310.common.world.view;

import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkListField;

@NetworkClass
public class WorldMapView {

    @NetworkListField(WorldSystemView.class)
    public List<WorldSystemView> systems;
    
}
