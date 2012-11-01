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

public class ReactorReactivityUpgradeEffect extends UpgradeEffect {

    @Override
    public void apply(UpgradeOwnership playerUpgrade) {
        Player player = playerUpgrade.getPlayer();
        List<Ship> shipList = player.getShipList();

        for (Ship ship : shipList) {
            for (Component component : ship.getComponents()) {
                List<LinearEngineCapacity> engineCapacities = component.getCapacitiesByClass(LinearEngineCapacity.class);
                for (LinearEngineCapacity engineCapacity : engineCapacities) {
                    if(engineCapacity.getName().equals("reactor")) {
                        double lastTheoricalMinThrust = engineCapacity.theoricalVariationSpeed;
    
                        if (playerUpgrade.getRank() > 0) {
                            engineCapacity.theoricalVariationSpeed *= (1 + 3 * 0.1 * Math.pow(2, playerUpgrade.getRank()));
                        }
                        if (engineCapacity.theoricalVariationSpeed != lastTheoricalMinThrust) {
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
        shipReactorThrustUpgrade.setGlobalDescription("Increase reactivity of all your reactors.");
        shipReactorThrustUpgrade.setTag("ship_upgrade.reactor_reactivity");
        shipReactorThrustUpgrade.setName("Reactor reactivity");
        shipReactorThrustUpgrade.addRank(50, "60% reactor reactivity increase.");
        shipReactorThrustUpgrade.addRank(200, "120% reactor reactivity increase.");
        shipReactorThrustUpgrade.addRank(800, "240% reactor reactivity increase.");
        shipReactorThrustUpgrade.addRank(1600, "480% reactor reactivity increase.");
        shipReactorThrustUpgrade.addRank(6400, "960% reactor reactivity increase.");
        return shipReactorThrustUpgrade;
    }

}
