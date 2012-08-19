package com.irr310.server.ai;

import com.irr310.common.Game;
import com.irr310.common.tools.Log;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.Component;
import com.irr310.common.world.Part;
import com.irr310.common.world.Ship;
import com.irr310.common.world.World;
import com.irr310.server.GameServer;

/**
 * Very basic AI: jsut rush on the monolith
 * 
 * @author fred
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

        driver.setPositionTarget(new Vec3(0, 0, 0));
        double currentDist = shipPosition.length() * 2;

        for (Ship ship : Game.getInstance().getWorld().getShips()) {
            if (ship.getOwner() != getShip().getOwner()) {
                Part enemyKernel = ship.getComponentByName("kernel").getFirstPart();
                TransformMatrix enemyShipTransform = enemyKernel.getTransform();
                Vec3 enemyShipPosition = enemyShipTransform.getTranslation();
                double shipDistance = enemyShipPosition.distanceTo(shipPosition);
                if (shipDistance < currentDist) {
                    currentDist = shipDistance;

                    // Find intersection
                    Vec3 enemySpeed = enemyKernel.getLinearSpeed();

                    /*
                     * if(enemySpeed.length() > maxSpeed) { //Not possible to
                     * it, follow the same axis
                     * driver.setPositionTarget(shipPosition.plus(enemySpeed));
                     * } else {
                     */
                    // Intesection possible
                    /*double dt = Math.sqrt(shipDistance / ((maxSpeed + kernel.getFirstPart().getLinearSpeed().length()) / 2.0));
                    Vec3 targetSpeed = new Vec3(0, 1, 0).rotate(enemyShipTransform).multiply(enemySpeed.length());

                    driver.setPositionTarget(enemyShipPosition.plus(targetSpeed.multiply(dt)));

                    // }
                    // A = enemy
                    // B = me
                    // D = A-B
                    

                        Vec3 A = enemyShipPosition;
                        Vec3 B = shipPosition;
                        double Bprime = maxSpeed;
                        double Aprime = enemySpeed.length();
                        Vec3 D = B.minus(A);
                        double Dlength = D.length();
                        double xaxdyayd = enemySpeed.x * D.x + enemySpeed.y * D.y + enemySpeed.z * D.z;

                        double sup1 = xaxdyayd + Math.sqrt(xaxdyayd * xaxdyayd + (Bprime * Bprime - Aprime * Aprime) * Dlength * Dlength);
                        double sup2 = xaxdyayd - Math.sqrt(xaxdyayd * xaxdyayd + (Bprime * Bprime - Aprime * Aprime) * Dlength * Dlength);
                        double inf = Bprime * Bprime - Aprime * Aprime;
                        double t1 = sup1 / inf;
                        double t2 = sup2 / inf;
                        Vec3 target1 = enemyShipPosition.plus(enemySpeed.multiply(t1));
                        Vec3 target2 = enemyShipPosition.plus(enemySpeed.multiply(t2));
                        driver.setPositionTarget(target1);

                        Log.trace("A=" + A);
                        Log.trace("B=" + B);
                        Log.trace("D=" + D);
                        Log.trace("Aprime=" + Aprime);
                        Log.trace("Bprime=" + Bprime);
                        Log.trace("Dlength=" + Dlength);
                        Log.trace("xaxdyayd=" + xaxdyayd);
                        Log.trace("sup=" + sup1);
                        Log.trace("inf=" + inf);
                        Log.trace("t1=" + t1);
                        Log.trace("target1=" + target1);
                        Log.trace("target1-A =" + (target1.minus(A)));
                        Log.trace("target1-B =" + (target1.minus(B)));
                        Log.trace("target1-A length =" + (target1.minus(A).length()));
                        Log.trace("target1-B length =" + (target1.minus(B).length()));
                        Log.trace("target1-A length time =" + (target1.minus(A).length() / Aprime));
                        Log.trace("target1-B length time =" + (target1.minus(B).length() / Bprime));

                        Log.trace("t2=" + t2);
                        Log.trace("target2=" + target2);
                        Log.trace("target2-A =" + (target2.minus(A)));
                        Log.trace("target2-B =" + (target2.minus(B)));
                        Log.trace("target2-A length =" + (target2.minus(A).length()));
                        Log.trace("target2-B length =" + (target2.minus(B).length()));
                        Log.trace("target2-A length time =" + (target2.minus(A).length() / Aprime));
                        Log.trace("target2-B length time =" + (target2.minus(B).length() / Bprime));

*/
                        Vec3 A = enemyShipPosition;
                        Vec3 B = shipPosition;
                        Vec3 D = B.minus(A);
                    
                        Vec3 targetSpeed = new Vec3(0, 1, 0).rotate(enemyShipTransform).multiply(enemySpeed.length());
                    
                        double commonSpeed = - targetSpeed.length() * ( D.normalize().dot(targetSpeed.normalize()));
                        double remainingSpeed = maxSpeed - commonSpeed;
                        
                        if (remainingSpeed < 0) {
                            // Not possible to it, follow the same axis
                            driver.setPositionTarget(shipPosition.plus(enemySpeed));
                        } else {
                        
                            
                            double Bprime = maxSpeed;
                            double Aprime = enemySpeed.length();
                            double Dlength = D.length();
                            
                            double t = Dlength / remainingSpeed;
                            Vec3 target = enemyShipPosition.plus(enemySpeed.multiply(t));
    
                            /*Log.trace("targetSpeed=" + targetSpeed);
                            Log.trace("targetSpeed normalize=" + targetSpeed.normalize());
                            Log.trace("D=" + D);
                            Log.trace("D normalize=" + D.normalize());
                            Log.trace("( D.normalize().dot(targetSpeed.normalize()))=" + ( D.normalize().dot(targetSpeed.normalize())));
                            Log.trace("commonSpeed=" + commonSpeed);
                            Log.trace("remainingSpeed=" + remainingSpeed);
                            Log.trace("t=" + t);
                            Log.trace("target=" + target);
                            Log.trace("target-A =" + (target.minus(A)));
                            Log.trace("target-B =" + (target.minus(B)));
                            Log.trace("target-A length =" + (target.minus(A).length()));
                            Log.trace("target-B length =" + (target.minus(B).length()));
                            Log.trace("target-A length time =" + (target.minus(A).length() / Aprime));
                            Log.trace("target-B length time =" + (target.minus(B).length() / Bprime));
                            Log.trace("done");*/
                            
                            driver.setPositionTarget(target);
    
                            driver.setPositionTarget(enemyShipPosition.plus(targetSpeed.multiply(t)));
                        }
                        
                        

                    /**
                     * x(t) = x(0) + vx * t y(t) = y(0) + vy * t
                     */

                }
            }
        }

        driver.processOrders();
    }

}
