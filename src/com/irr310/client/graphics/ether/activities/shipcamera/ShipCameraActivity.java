package com.irr310.client.graphics.ether.activities.shipcamera;

import com.irr310.common.event.system.DefaultSystemEventVisitor;
import com.irr310.common.event.system.QuerySystemStateEvent;
import com.irr310.common.event.system.SystemStateEvent;
import com.irr310.common.world.state.EntityState;
import com.irr310.common.world.state.ShipState;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.server.GameServer;
import com.irr310.server.SystemEngine;

public class ShipCameraActivity extends Activity {

    private RelativeLayout mainRelativeLayout;
    private WorldSystemState system;
    private ShipState ship;
    private WorldSystemState systemState;
    private SystemEngine systemEngine;
    private DefaultSystemEventVisitor visitor;
    private static final int UPDATE_SYSTEM_WHAT = 1;

    @Override
    protected void onCreate(Bundle bundle) {
setContentView("main@layout/camera/ship_camera");
        
        mainRelativeLayout = (RelativeLayout) findViewById("main@layout/camera/ship_camera");
        
        ShipCameraActivityBundle shipCameraActivityBundle = (ShipCameraActivityBundle) bundle;
        system = shipCameraActivityBundle.getSystem();
        ship = shipCameraActivityBundle.getShip();
        
        systemEngine = GameServer.getWorldEngine().getSystemEngine(system);
        
        visitor = new DefaultSystemEventVisitor() {
            

            @Override
            public void visit(SystemStateEvent event) {
                systemState = event.getSystemState();
                getHandler().obtainMessage(UPDATE_SYSTEM_WHAT).send();
            }
            
            
        };
        
    }


    @Override
    public void onResume() {
        systemEngine.registerEventVisitor(visitor);
        systemEngine.sendToAll(new QuerySystemStateEvent(system, EntityState.FULL_DEPTH));
    }

    @Override
    public void onPause() {
        systemEngine.unregisterEventVisitor(visitor);
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
        mainRelativeLayout.addViewInLayout(new ShipCameraView(ship));
    }

    public static class ShipCameraActivityBundle extends Bundle {

        static class ShipCameraData {
            public ShipCameraData(WorldSystemState system, ShipState ship) {
                this.system = system;
                this.ship = ship;
            }
            public WorldSystemState system;
            public ShipState ship;
        }
        
        public ShipCameraActivityBundle(WorldSystemState system, ShipState ship) {
            super(new ShipCameraData(system, ship));
        }
        
        WorldSystemState getSystem() {
            return ((ShipCameraData) getObject()).system;
        }
        
        ShipState getShip() {
            return ((ShipCameraData) getObject()).ship;
        }
    }
    
}
