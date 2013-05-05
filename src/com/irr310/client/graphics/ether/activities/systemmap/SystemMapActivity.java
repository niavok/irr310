package com.irr310.client.graphics.ether.activities.systemmap;

import com.irr310.common.event.system.DefaultSystemEventVisitor;
import com.irr310.common.event.system.QuerySystemStateEvent;
import com.irr310.common.event.system.SystemStateEvent;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.server.GameServer;
import com.irr310.server.SystemEngine;

public class SystemMapActivity extends Activity {
    private SystemEngine systemEngine;
    private DefaultSystemEventVisitor visitor;
    WorldSystemState systemState;
    private RelativeLayout systemDetailsRelativeLayout;

    private static final int UPDATE_SYSTEM_WHAT = 1;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/system_map/system_map");
        
        systemDetailsRelativeLayout = (RelativeLayout) findViewById("systemDetailsRelativeLayout@layout/system_map/system_map");
        
        WorldSystemState system = (WorldSystemState) bundle.getObject();
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
        systemEngine.sendToAll(new QuerySystemStateEvent());
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
    
    public static class SystemMapActivityBundle extends Bundle {

        public SystemMapActivityBundle(WorldSystemState obj) {
            super(obj);
        }
        
        WorldSystemState getSystem() {
            return (WorldSystemState) getObject();
        }
    }
    
    protected void updateAll() {
        systemDetailsRelativeLayout.removeAllView();
        systemDetailsRelativeLayout.addViewInLayout(new SystemDetailsView(systemState));
    }
    
}
