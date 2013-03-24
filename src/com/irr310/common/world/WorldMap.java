package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.common.world.view.FactionView;
import com.irr310.common.world.view.WorldMapView;
import com.irr310.common.world.view.WorldSystemView;

public class WorldMap {

    List<WorldSystem> systems = new ArrayList<WorldSystem>();
    
    public void addZone(WorldSystem zone) {
        systems.add(zone);
    }

    public WorldSystem nearestSystemTo(Vec2 location) {
        WorldSystem nearestZone = null;
        double nearestDistance = 0;
        
        for(WorldSystem zone : systems) {
            double localDistance = zone.getLocation().distanceTo(location);
            if(nearestZone == null || localDistance < nearestDistance) {
                nearestZone = zone;
                nearestDistance = localDistance;
            } 
        }
        
        return nearestZone;
    }

    public void dump() {
        String points = "";
        for(WorldSystem zone : systems) {
            points += "["+zone.getLocation().x+","+zone.getLocation().y+"],";
        }
        Log.trace(points);
    }

    public List<WorldSystem> getSystems() {
        return systems;
    }

    public WorldMapView toView() {
        WorldMapView worldMapView = new WorldMapView();
        worldMapView.systems = new ArrayList<WorldSystemView>();
        for(WorldSystem system: systems) {
            worldMapView.systems.add(system.toView());
        }
        
        return worldMapView;
    }
}
