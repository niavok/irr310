package com.irr310.common.world.item;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Faction;
import com.irr310.common.world.World;
import com.irr310.common.world.system.Nexus;
import com.irr310.server.game.BuildingFactory;

public class NexusItem extends DeployableItem {

   
    public NexusItem(World world, long id, Faction owner) {
        super(world, id, owner);
        setItemType(ItemType.BUILDING);
        setDeployedRadius(10);
    }

    @Override
    protected void doDeploy(Vec3 position) {
        
        Nexus nexus= new BuildingFactory(getWorld()).createNexus();
        nexus.getFirstPart().getTransform().translate(position);

        currentSystem.addCelestialObject(nexus);
    }

}
