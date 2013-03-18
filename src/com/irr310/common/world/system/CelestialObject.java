package com.irr310.common.world.system;

import com.irr310.common.world.World;
import com.irr310.common.world.view.CelestialObjectView;
import com.irr310.common.world.view.PartView;


public class  CelestialObject extends WorldObject {

	
	
	public CelestialObject(World world, long id, String name) {
	    super(world, id, name);
	}

    public CelestialObjectView toView() {
        CelestialObjectView celestialObjectView = new CelestialObjectView();
        
        celestialObjectView.id = getId();
        celestialObjectView.name = getName();
        
        // WorldObject properties    
        celestialObjectView.skin = getSkin();
        celestialObjectView.durabilityMax = getDurabilityMax();
        celestialObjectView.durability = getDurability();
        celestialObjectView.physicalResistance = getPhysicalResistance();
        celestialObjectView.heatResistance = getHeatResistance();

        for(Part part: parts) {
            celestialObjectView.parts.add(part.toView());    
        }
        
        return celestialObjectView;
    }

    public void fromView(CelestialObjectView celestialObjectView) {

        // World objectProperties
        setSkin(celestialObjectView.skin);
        setDurabilityMax(celestialObjectView.durabilityMax);
        setDurability(celestialObjectView.durability);
        setPhysicalResistance(celestialObjectView.physicalResistance);
        setHeatResistance(celestialObjectView.heatResistance);

        
        for(PartView part: celestialObjectView.parts) {
            addPart(system.loadPart(part, this));
        }
    }
}
