package com.irr310.common.world.system;

import com.irr310.common.world.state.CelestialObjectState;
import com.irr310.common.world.state.PartState;


public class  CelestialObject extends SystemObject {

	
	
	public CelestialObject(WorldSystem system, long id, String name) {
	    super(system, id, name);
	}

    public CelestialObjectState toState() {
        CelestialObjectState celestialObjectState = new CelestialObjectState();
        
        celestialObjectState.id = getId();
        celestialObjectState.name = getName();
        
        // WorldObject properties    
        celestialObjectState.skin = getSkin();
        celestialObjectState.durabilityMax = getDurabilityMax();
        celestialObjectState.durability = getDurability();
        celestialObjectState.physicalResistance = getPhysicalResistance();
        celestialObjectState.heatResistance = getHeatResistance();

        for(Part part: parts) {
            celestialObjectState.parts.add(part.toState());    
        }
        
        return celestialObjectState;
    }

    public void fromState(CelestialObjectState celestialObjectState) {

        // World objectProperties
        setSkin(celestialObjectState.skin);
        setDurabilityMax(celestialObjectState.durabilityMax);
        setDurability(celestialObjectState.durability);
        setPhysicalResistance(celestialObjectState.physicalResistance);
        setHeatResistance(celestialObjectState.heatResistance);

        
        for(PartState part: celestialObjectState.parts) {
            addPart(getSystem().loadPart(part, this));
        }
    }
}
