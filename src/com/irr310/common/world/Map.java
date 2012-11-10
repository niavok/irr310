package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.common.world.system.WorldSystem;

public class Map {

    List<WorldSystem> zones = new ArrayList<WorldSystem>();
    
    public void addZone(WorldSystem zone) {
        zones.add(zone);
    }

    public WorldSystem nearestSystemTo(Vec2 location) {
        WorldSystem nearestZone = null;
        double nearestDistance = 0;
        
        for(WorldSystem zone : zones) {
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
        for(WorldSystem zone : zones) {
            points += "["+zone.getLocation().x+","+zone.getLocation().y+"],";
        }
        Log.trace(points);
    }

    public List<WorldSystem> getZones() {
        return zones;
    }

}
