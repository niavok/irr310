package com.irr310.client.graphics.ether.activities.worldmap;

import java.util.List;

import org.newdawn.slick.util.Log;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.world.Faction;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.ScrollView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnMouseEventListener;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class WorldMapActivity extends Activity {

    
    private RelativeLayout map;
    private Faction faction;
    private ScrollView mapScrollView;
    private boolean firstUpdate = true;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/world_map");
        map = (RelativeLayout) findViewById("map@layout/world_map");
        mapScrollView = (ScrollView) findViewById("mapScrollView@layout/world_map");
        
        World world = (World) bundle.getObject();
        
        Player player = LoginManager.getLocalPlayer();
        
        faction = player.getFaction();
        List<WorldSystem> knownSystems = faction.getKnownSystems();
        
        for (WorldSystem system : world.getMap().getSystems()) {
            map.addChild(new SystemView(system));
        }
        
        
        map.setOnMouseListener(new OnMouseEventListener() {
            
            @Override
            public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
                //if(mouseEvent.getButton() )
                Log.debug("Button "+ mouseEvent.getButton());
                Log.debug("Action "+ mouseEvent.getAction());
                return false;
            }
        });
        
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Time absTime, Time gameTime) {
        if(firstUpdate ) {
            firstUpdate = false;
            WorldSystem homeSystem = faction.getHomeSystem();
            mapScrollView.setCenterScroll((float)homeSystem.getLocation().x * 3, (float)homeSystem.getLocation().y *3 );
//            mapScrollView.setCenterScroll(0,0);
        }
    }

}
