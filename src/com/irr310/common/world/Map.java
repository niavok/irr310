package com.irr310.common.world;

import java.util.ArrayList;
import java.util.List;

import com.irr310.common.engine.Engine;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.Vec2;
import com.irr310.common.world.zone.Zone;

public class Map {

    List<Zone> zones = new ArrayList<Zone>();
    
    public void addZone(Zone zone) {
        zones.add(zone);
    }

    public Zone nearestZoneTo(Vec2 location) {
        Zone nearestZone = null;
        double nearestDistance = 0;
        
        for(Zone zone : zones) {
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
        for(Zone zone : zones) {
            points += "["+zone.getLocation().x+","+zone.getLocation().y+"],";
        }
        Log.trace(points);
    }

    public List<Zone> getZones() {
        return zones;
    }

}
