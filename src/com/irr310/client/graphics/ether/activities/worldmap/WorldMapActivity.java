package com.irr310.client.graphics.ether.activities.worldmap;

import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.util.Log;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.world.Faction;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Point;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.ScrollView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnMouseEventListener;
import com.irr310.server.Time;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class WorldMapActivity extends Activity {

    
    private RelativeLayout map;
    private Faction faction;
    private ScrollView mapScrollView;
    private boolean firstUpdate = true;
    private float zoom;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/world_map");
        map = (RelativeLayout) findViewById("map@layout/world_map");
        mapScrollView = (ScrollView) findViewById("mapScrollView@layout/world_map");
        
        World world = (World) bundle.getObject();
        
        Player player = LoginManager.getLocalPlayer();
        
        faction = player.getFaction();
        List<WorldSystem> knownSystems = faction.getKnownSystems();
        
        zoom = 1f;
        
        List<WorldSystem> allSystems = world.getMap().getSystems();
        for (WorldSystem system : allSystems) {
            SystemView view = new SystemView(system);
            view.setZoom(zoom);
            map.addChild(view);
        }
        
        
        map.setOnMouseListener(new OnMouseEventListener() {
            
            @Override
            public boolean onMouseEvent(V3DMouseEvent mouseEvent) {
                
                Point point = new Point(mouseEvent.getX(), mouseEvent.getY());
                
                if(mouseEvent.getAction() == Action.MOUSE_WHEEL_UP) {
                    zoom(point, 1.1f);
                } else if(mouseEvent.getAction() == Action.MOUSE_WHEEL_DOWN) {
                    zoom(point, 1/1.1f);
                }
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
            Log.debug("Home system at " + homeSystem.getLocation());
            mapScrollView.setScrollCenter(new Point((float)homeSystem.getLocation().x * zoom, (float)homeSystem.getLocation().y *zoom ));
//            mapScrollView.setCenterScroll(0,0);
        }
    }
    
    private void zoom(Point point, float zoomFactor) {
        
        Log.debug("Zoom mouse=" + point +" zoomFactor="+zoomFactor);
        
        Point mousePoint =  point.minus(mapScrollView.getScrollOffset());
        
        Point staticPointBase = mousePoint.minus(mapScrollView.getScrollOffset()).divide(zoom);
        
        Log.debug("mousePoint=" + mousePoint);
        Log.debug("staticPointBase=" + staticPointBase);
        
        zoom *= zoomFactor; 
        
        for (View child : map.getChildren()) {
            if(child instanceof SystemView) {
                SystemView systemView = (SystemView) child;
                systemView.setZoom(zoom);
            }
        }

        mapScrollView.setScrollOffset(mousePoint.minus(staticPointBase.multiply(zoom)));
    }
    
}
