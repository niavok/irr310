package com.irr310.common.world.state;

import java.util.List;

import com.irr310.common.network.NetworkClass;
import com.irr310.common.network.NetworkListField;

@NetworkClass
public class WorldMapState {

    @NetworkListField(WorldSystemState.class)
    public List<WorldSystemState> systems;
    
}
