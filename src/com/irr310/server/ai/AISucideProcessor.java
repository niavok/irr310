package com.irr310.server.ai;

import com.irr310.common.Game;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
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
        double maxSpeed = getShip().getMaxSpeed(false);
        
        driver.setPositionTarget(new  Vec3(0,0,0));
        double currentDist = shipPosition.length()*2;
        
        for(Ship ship: Game.getInstance().getWorld().getShips()){
            if(ship.getOwner() != getShip().getOwner()) {
                Part enemyKernel = ship.getComponentByName("kernel").getFirstPart();
                TransformMatrix enemyShipTransform = enemyKernel.getTransform();
                Vec3 enemyShipPosition = enemyShipTransform.getTranslation();
                double shipDistance = enemyShipPosition.distanceTo(shipPosition);
                if(shipDistance < currentDist)  {
                    currentDist = shipDistance;

                    // Find intersection 
                     Vec3 enemySpeed = enemyKernel.getLinearSpeed();
                    
                     /*if(enemySpeed.length() > maxSpeed) {
                         //Not possible to it, follow the same axis
                         driver.setPositionTarget(shipPosition.plus(enemySpeed));     
                     } else {*/
                         // Intesection possible
                         double dt = Math.sqrt(shipDistance / ((maxSpeed + kernel.getFirstPart().getLinearSpeed().length())/2.0));
                         Vec3 targetSpeed = new Vec3(0, 1, 0).rotate(enemyShipTransform).multiply(enemySpeed.length());
                         
                         driver.setPositionTarget(enemyShipPosition.plus(targetSpeed.multiply(dt)));
                         
                     //}
                    /**
                     * x(t) = x(0) + vx * t 
                     * y(t) = y(0) + vy * t
                     * 
                     * 
                     * 
                     */
                    
                    
                }
            }
        }
        
        
        driver.processOrders();
    }

}
