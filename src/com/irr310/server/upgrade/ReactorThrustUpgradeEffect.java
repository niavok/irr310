package com.irr310.server.upgrade;

import java.util.List;

import com.irr310.common.world.Player;
import com.irr310.common.world.capacity.BalisticWeaponCapacity;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.capacity.RocketWeaponCapacity;
import com.irr310.common.world.system.Component;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.upgrade.Upgrade;
import com.irr310.common.world.upgrade.Upgrade.UpgradeCategory;
import com.irr310.common.world.upgrade.UpgradeOwnership;

public class ReactorThrustUpgradeEffect extends UpgradeEffect {

    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        List<Ship> shipList = player.getShipList();

        for (Ship ship : shipList) {
            for (Component component : ship.getComponents()) {
                List<LinearEngineCapacity> engineCapacities = component.getCapacitiesByClass(LinearEngineCapacity.class);
                for (LinearEngineCapacity engineCapacity : engineCapacities) {
                    if(engineCapacity.getName().equals("reactor")) {
                        double lastTheoricalMaxThrust = engineCapacity.theoricalMaxThrust;
    
                        if (playerUpgrade.getRank() > 0) {
                            engineCapacity.theoricalMaxThrust *= (1 + 0.1 * Math.pow(2, playerUpgrade.getRank()));
                            engineCapacity.theoricalVariationSpeed *= (1 + 0.25 * 0.1 * Math.pow(2, playerUpgrade.getRank()));
                        }
                        if (engineCapacity.theoricalMaxThrust != lastTheoricalMaxThrust) {
                            // TODO: network !
                        }
                    }

                }


            }
        }
    }

    @Override
    public Upgrade generateUpgrade() {
        Upgrade shipReactorThrustUpgrade = new Upgrade();
        shipReactorThrustUpgrade.setCategory(UpgradeCategory.SHIP_UPGRADE);
        shipReactorThrustUpgrade.setGlobalDescription("Increase thrust of all your reactors.");
        shipReactorThrustUpgrade.setTag("ship_upgrade.reactor_thrust");
        shipReactorThrustUpgrade.setName("Reactor thrust");
        shipReactorThrustUpgrade.addRank(50, "20% reactor thrust increase.");
        shipReactorThrustUpgrade.addRank(200, "40% reactor thrust increase.");
        shipReactorThrustUpgrade.addRank(800, "80% reactor thrust increase.");
        shipReactorThrustUpgrade.addRank(1600, "160% reactor thrust increase.");
        shipReactorThrustUpgrade.addRank(6400, "320% reactor thrust increase.");
        return shipReactorThrustUpgrade;
    }

}
