package com.irr310.client.graphics.ether.activities.shipcamera;

import java.util.List;

import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.capacity.LinearEngineCapacity;
import com.irr310.common.world.system.CelestialObject;
import com.irr310.common.world.system.Component;
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
    private WorldSystem mSystem;
    private Ship ship;
    private SystemEngine systemEngine;
    private SystemEngineObserver mSystemEngineObserver;
    private static final int UPDATE_SYSTEM_WHAT = 1;

    @Override
    protected void onCreate(Bundle bundle) {
        setContentView("main@layout/camera/ship_camera");
        
        mainRelativeLayout = (RelativeLayout) findViewById("main@layout/camera/ship_camera");
        
        ShipCameraActivityBundle shipCameraActivityBundle = (ShipCameraActivityBundle) bundle;
        mSystem = shipCameraActivityBundle.getSystem();
        ship = shipCameraActivityBundle.getShip();
        
        
        systemEngine = GameServer.getWorldEngine().getSystemEngine(mSystem);
        
        
        mSystemEngineObserver = new SystemEngineObserver() {
            
            @Override
            public void onDeployShip(Ship ship, TransformMatrix transform) {
            }

            @Override
            public void onDeployCelestialObject(CelestialObject celestialObject, TransformMatrix transform) {

            }
        };
        
    }

    @Override
    public void onResume() {
        systemEngine.getSystemEnginObservable().register(this, mSystemEngineObserver);
        updateAll();
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
        mainRelativeLayout.removeAllView();
        mainRelativeLayout.addViewInLayout(new ShipCameraView(ship, systemEngine));
        mainRelativeLayout.addViewInLayout(new ShipDriverView(ship, systemEngine));
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
