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
import com.irr310.i3d.view.LinearLayout;
import com.irr310.i3d.view.Point;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.ScrollView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
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
    private float defzoom = 8f;
    private SystemView selection;
    private TextView selectedSystemTitle;
    private LinearLayout selectedSystemPanel;
    private ScrollZone zone;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/world_map");
        map = (RelativeLayout) findViewById("map@layout/world_map");
        mapScrollView = (ScrollView) findViewById("mapScrollView@layout/world_map");
        selectedSystemTitle = (TextView) findViewById("selectedSystemTitle@layout/world_map");
        selectedSystemPanel = (LinearLayout) findViewById("selectedSystemPanel@layout/world_map");

        World world = (World) bundle.getObject();

        Player player = LoginManager.getLocalPlayer();

        faction = player.getFaction();
        List<WorldSystem> knownSystems = faction.getKnownSystems();

        zoom = defzoom;
        zone = new ScrollZone();

        List<WorldSystem> allSystems = world.getMap().getSystems();
        for (WorldSystem system : allSystems) {
            final SystemView systemView = new SystemView(system);            
            //systemView.setZoom(zoom);
            map.addChild(systemView);
            systemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    selectSystem(systemView);
                }

            });
            zone.setZone(system.getLocation());
        }
        // call zoom function in order to initialize zoom on each system
        // zoom factor will be 1.0
        this.zoom(new Point(0,0), 1.0f);

        map.setOnMouseListener(new OnMouseEventListener() {

            @Override
            public boolean onMouseEvent(V3DMouseEvent mouseEvent) {

                Point point = new Point(mouseEvent.getX(), mouseEvent.getY());

                if (mouseEvent.getAction() == Action.MOUSE_WHEEL_UP) {
                    zoom(point, 1.1f);
                } else if (mouseEvent.getAction() == Action.MOUSE_WHEEL_DOWN) {
                    zoom(point, 1 / 1.1f);
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
        if (firstUpdate) {
            firstUpdate = false;
            WorldSystem homeSystem = faction.getHomeSystem();
            Log.debug("Home system at " + homeSystem.getLocation());
            mapScrollView.setScrollCenter(new Point((float) homeSystem.getLocation().x * zoom, (float) homeSystem.getLocation().y * zoom));
            // mapScrollView.setCenterScroll(0,0);
        }
    }

    private void selectSystem(SystemView systemView) {
        if (selection != null) {
            selection.setSelected(false);
        }
        selection = systemView;
        systemView.setSelected(true);
        
        if(selection == null) {
            selectedSystemPanel.setVisible(false);
        } else {
            selectedSystemTitle.setText(selection.getSystem().getName());
            selectedSystemPanel.setVisible(true);
        }
    }

    private void zoom(Point point, float zoomFactor) {

        Point mousePoint = point.add(mapScrollView.getScrollOffset());

        Point staticPointBase = mousePoint.minus(mapScrollView.getScrollOffset()).divide(zoom);

        zoom *= zoomFactor;
        if ( zoom > defzoom ) { zoom = defzoom; }
        if ( zoom < 0.5f ) { zoom = 0.5f; }

        for (View child : map.getChildren()) {
            if (child instanceof SystemView) {
                SystemView systemView = (SystemView) child;
                systemView.setZoom(zoom);
            }
        }
        // define min and max for scrollzone (min = bot/right corner, max = top/left corner)
        mapScrollView.setScrollZone(new Point(zone.getMin(zoom)), new Point(zone.getMax(zoom)));
        mapScrollView.setScrollOffset(mousePoint.minus(staticPointBase.multiply(zoom)));
    }

}
