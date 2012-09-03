package com.irr310.client.script;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.tools.Vec3;
import com.irr310.common.world.zone.Ship;
import com.irr310.server.ai.SimpleShipDriver;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class FighterDriver implements ClientGuiDriver {

    private Ship ship;
    private SimpleShipDriver driver;

    @Override
    public void init() {
        ship = LoginManager.getLocalPlayer().getShipList().get(0);
        driver = new SimpleShipDriver(ship);
    }

    @Override
    public void keyRelease(int keyCode, String character) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(int keyCode, String character) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEvent(V3DMouseEvent mouseEvent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void frame() {
        // Ship commands
        driver.setTargetSpeed(ship.getMaxSpeed(false));
        driver.setPositionTarget(new Vec3(100, 25, 60));
        
        driver.processOrders();
    }

}
