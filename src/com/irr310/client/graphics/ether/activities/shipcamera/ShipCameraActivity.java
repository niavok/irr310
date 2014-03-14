package com.irr310.client.graphics.ether.activities.shipcamera;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.server.GameServer;
import com.irr310.server.engine.system.SystemEngine;
import com.irr310.server.engine.system.SystemEngineObserver;
import com.sun.org.apache.xerces.internal.impl.validation.EntityState;

public class ShipCameraActivity extends Activity {

    private RelativeLayout mainRelativeLayout;
    private WorldSystem system;
    private Ship ship;
    private WorldSystem systemState;
    private SystemEngine systemEngine;
    private SystemEngineObserver mSystemEngineObserver;
    private static final int UPDATE_SYSTEM_WHAT = 1;

    @Override
    protected void onCreate(Bundle bundle) {
setContentView("main@layout/camera/ship_camera");
        
        mainRelativeLayout = (RelativeLayout) findViewById("main@layout/camera/ship_camera");
        
        ShipCameraActivityBundle shipCameraActivityBundle = (ShipCameraActivityBundle) bundle;
        system = shipCameraActivityBundle.getSystem();
        ship = shipCameraActivityBundle.getShip();
        
        systemEngine = GameServer.getWorldEngine().getSystemEngine(system);
        
        
        mSystemEngineObserver = new SystemEngineObserver() {
            
            @Override
            public void onDeployShip(Ship ship, TransformMatrix transform) {
            }
        };
        
    }

    @Override
    public void onResume() {
        systemEngine.getSystemEnginObservable().register(this, mSystemEngineObserver);
    }

    @Override
    public void onPause() {
        systemEngine.getSystemEnginObservable().unregister(this);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    protected void onMessage(Message message) {
        switch (message.what) {
            case UPDATE_SYSTEM_WHAT:
                updateAll();
                break;
        }
    }

    private void updateAll() {
        mainRelativeLayout.addViewInLayout(new ShipCameraView(ship, systemState));
    }

    public static class ShipCameraActivityBundle extends Bundle {

        static class ShipCameraData {
            public ShipCameraData(WorldSystem system, Ship ship) {
                this.system = system;
                this.ship = ship;
            }
            public WorldSystem system;
            public Ship ship;
        }
        
        public ShipCameraActivityBundle(WorldSystem system, Ship ship) {
            super(new ShipCameraData(system, ship));
        }
        
        WorldSystem getSystem() {
            return ((ShipCameraData) getObject()).system;
        }
        
        Ship getShip() {
            return ((ShipCameraData) getObject()).ship;
        }
    }
    
}
