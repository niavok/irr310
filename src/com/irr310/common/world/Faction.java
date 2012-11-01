package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.world.system.GameEntity;
import com.irr310.common.world.system.System;

public class Faction extends GameEntity{

    private System homeSystem;
    private List<System> knownSystems = new ArrayList<System>();

    public Faction(long id) {
        super(id);
    }

    public void setHomeSystem(System system) {
        this.homeSystem = system;
        discoverSystem(system);
    }

    private void discoverSystem(System system) {
        knownSystems.add(system);
    }
    
    public System getHomeSystem() {
        return homeSystem;
    }

}
