package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.engine.Engine;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.common.world.system.System;

public class Map {

    List<System> zones = new ArrayList<System>();
    
    public void addZone(System zone) {
        zones.add(zone);
    }

    public System nearestSystemTo(Vec2 location) {
        System nearestZone = null;
        double nearestDistance = 0;
        
        for(System zone : zones) {
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
        for(System zone : zones) {
            points += "["+zone.getLocation().x+","+zone.getLocation().y+"],";
        }
        Log.trace(points);
    }

    public List<System> getZones() {
        return zones;
    }

}
