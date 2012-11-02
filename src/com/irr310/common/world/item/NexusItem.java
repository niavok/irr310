package com.irr310.common.world.item;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.system.Nexus;
import com.irr310.server.game.BuildingFactory;

public class NexusItem extends DeployableItem {

   
    public NexusItem(long id, Faction owner) {
        super(id, owner);
        setItemType(ItemType.BUILDING);
        setDeployedRadius(10);
    }

    @Override
    protected void doDeploy(Vec3 position) {
        
        Nexus nexus= BuildingFactory.createNexus();
        nexus.getFirstPart().getTransform().translate(position);

        currentSystem.addCelestialObject(nexus);
    }

}
