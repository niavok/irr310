package com.irr310.common.world;

import com.irr310.common.Game;
import com.irr310.common.world.view.CelestialObjectView;
import com.irr310.common.world.view.PartView;


public class  CelestialObject extends WorldObject {

	
	
	public CelestialObject(long id, String name) {
	    super(id, name);
	}

    public CelestialObjectView toView() {
        CelestialObjectView freeObjectView = new CelestialObjectView();
        
        freeObjectView.id = getId();
        freeObjectView.name = getName();
        freeObjectView.skin = getSkin();
        
        for(Part part: parts) {
            freeObjectView.parts.add(part.toView());    
        }
        
        return freeObjectView;
    }

    public void fromView(CelestialObjectView CelestialObjectView) {
        World world = Game.getInstance().getWorld();
        setSkin(CelestialObjectView.skin);
        
        for(PartView part: CelestialObjectView.parts) {
            addPart(world.loadPart(part));
        }
    }
}
