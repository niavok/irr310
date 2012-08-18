package com.irr310.server.ai;

import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Ship;

/**
 * Very basic AI: jsut rush on the monolith
 * @author fred
 *
 */
public class AISucideProcessor extends AIProcessor {

    private final SimpleShipDriver driver;

    public AISucideProcessor(Ship ship) {
        super(ship);
        this.driver = new SimpleShipDriver(ship);
    }

    @Override
    public void process() {
        
        // Go to max speed
        driver.setTargetSpeed(driver.getMaxSpeed()/2);

        // Go to center
        driver.setPositionTarget(new  Vec3(0,0,0));
        
        driver.processOrders();
    }

}
