package com.irr310.server.ai;

import com.irr310.common.Game;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Ship;
import com.irr310.common.world.World;
import com.irr310.server.GameServer;

/**
 * Very basic AI: jsut rush on the monolith
 * @author fred
 *
 */
public class AISucideProcessor extends AIProcessor {

    private final SimpleShipDriver driver;
    private Component kernel;

    public AISucideProcessor(Ship ship) {
        super(ship);
        this.driver = new SimpleShipDriver(ship);
        kernel = getShip().getComponentByName("kernel");
        
    }

    @Override
    public void process() {
        
        // Go to max speed
        driver.setTargetSpeed(driver.getMaxSpeed());

        
        // Go to center or to the player
        Vec3 shipPosition = kernel.getFirstPart().getTransform().getTranslation();
        
        driver.setPositionTarget(new  Vec3(0,0,0));
        double currentDist = shipPosition.length();
        
        for(Ship ship: Game.getInstance().getWorld().getShips()){
            if(ship.getOwner() != getShip().getOwner()) {
                Vec3 ennemieShipPosition = ship.getComponentByName("kernel").getFirstPart().getTransform().getTranslation();
                double shipDistance = ennemieShipPosition.distanceTo(shipPosition);
                if(shipDistance < currentDist)  {
                    currentDist = shipDistance;
                    driver.setPositionTarget(ennemieShipPosition);
                }
            }
        }
        
        
        driver.processOrders();
    }

}
