package com.irr310.client.graphics.ether.activities.systemmap;

import com.irr310.client.graphics.ether.activities.shipcamera.ShipCameraActivity;
import com.irr310.client.graphics.ether.activities.shipcamera.ShipCameraActivity.ShipCameraActivityBundle;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.server.GameServer;
import com.irr310.server.engine.system.SystemEngine;
import com.irr310.server.engine.system.SystemEngineObserver;

public class SystemMapActivity extends Activity {
    private SystemEngine systemEngine;
    private RelativeLayout systemDetailsRelativeLayout;

    private static final int UPDATE_SYSTEM_WHAT = 1;
    private static final int UPDATE_SYSTEM_CONTENT_WHAT = 1;
    private WorldSystem mSystem;
    private SystemEngineObserver mSystemEngineObserver;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/system_map/system_map");
        
        systemDetailsRelativeLayout = (RelativeLayout) findViewById("systemDetailsRelativeLayout@layout/system_map/system_map");
        
        mSystem = (WorldSystem) bundle.getObject();
        systemEngine = GameServer.getWorldEngine().getSystemEngine(mSystem);
        
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
    
    public static class SystemMapActivityBundle extends Bundle {

        public SystemMapActivityBundle(WorldSystem obj) {
            super(obj);
        }
        
        WorldSystem getSystem() {
            return (WorldSystem) getObject();
        }
    }
    
    protected void updateAll() {
        systemDetailsRelativeLayout.removeAllView();
        systemDetailsRelativeLayout.addViewInLayout(new SystemDetailsView(this, mSystem));
    }


    public void connectShip(Ship ship) {
        ShipCameraActivityBundle bundle = new ShipCameraActivityBundle(mSystem, ship);
        startActivity(new Intent(ShipCameraActivity.class, bundle));
    }
    
}
