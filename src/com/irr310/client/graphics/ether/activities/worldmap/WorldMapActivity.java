package com.irr310.client.graphics.ether.activities.worldmap;

import java.util.Iterator;
import java.util.List;

import org.newdawn.slick.util.Log;

import com.irr310.client.navigation.LoginManager;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.FactionStateEvent;
import com.irr310.common.event.world.QueryFactionStateEvent;
import com.irr310.common.event.world.QueryWorldMapStateEvent;
import com.irr310.common.event.world.WorldEventDispatcher;
import com.irr310.common.event.world.WorldEventVisitor;
import com.irr310.common.event.world.WorldMapStateEvent;
import com.irr310.common.world.Faction;
import com.irr310.common.world.Player;
import com.irr310.common.world.World;
import com.irr310.common.world.state.FactionState;
import com.irr310.common.world.state.WorldMapState;
import com.irr310.common.world.state.WorldSystemState;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Handler;
import com.irr310.i3d.Message;
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
    private ScrollView mapScrollView;
    private boolean firstUpdate = true;
    private float zoom;
    private SystemView selection;
    private TextView selectedSystemTitle;
    private LinearLayout selectedSystemPanel;
    private Handler handler = new Handler();
    private WorldEventVisitor visitor;
    private WorldEventDispatcher worldEngine;
    private FactionState faction;
    private WorldMapState worldMap;
    private static final int UPDATE_FACTION_WHAT = 1;
    private static final int UPDATE_MAP_WHAT = 2;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/world_map");
        map = (RelativeLayout) findViewById("map@layout/world_map");
        mapScrollView = (ScrollView) findViewById("mapScrollView@layout/world_map");
        selectedSystemTitle = (TextView) findViewById("selectedSystemTitle@layout/world_map");
        selectedSystemPanel = (LinearLayout) findViewById("selectedSystemPanel@layout/world_map");

        zoom = 8f;

        worldEngine = (WorldEventDispatcher) bundle.getObject();

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

        visitor = new DefaultWorldEventVisitor() {

            @Override
            public void visit(FactionStateEvent event) {
                if(LoginManager.getLocalPlayer().faction.id == event.getFaction().id) {
                    handler.obtainMessage(UPDATE_FACTION_WHAT, event.getFaction()).send();
                }
            }

            @Override
            public void visit(WorldMapStateEvent event) {
                handler.obtainMessage(UPDATE_MAP_WHAT, event.getWorldMap()).send();
            }
        };

    }

    @Override
    public void onResume() {
        worldEngine.registerEventVisitor(visitor);
        worldEngine.sendToAll(new QueryWorldMapStateEvent());
        worldEngine.sendToAll(new QueryFactionStateEvent(LoginManager.getLocalPlayer().faction));
    }

    @Override
    public void onPause() {
        worldEngine.unregisterEventVisitor(visitor);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Time absTime, Time gameTime) {

        while (handler.hasMessages()) {
            Message message = handler.getMessage();

            switch (message.what) {
                case UPDATE_FACTION_WHAT:
                    faction = (FactionState) message.obj;
                    updateMap();
                    break;
                case UPDATE_MAP_WHAT:
                    worldMap = (WorldMapState) message.obj;
                    updateMap();
                    break;
            }

        }
    }

    private void updateMap() {

        if (faction == null || worldMap == null) {
            return;
        }

        map.removeAllView();
        
        WorldSystemState homeSystem = null;

        List<WorldSystemState> allSystems = worldMap.systems;
        for (WorldSystemState system : allSystems) {
            if (system.id == faction.homeSystemId) {
                homeSystem = system;
            }
            final SystemView systemView = new SystemView(system);
            systemView.setZoom(zoom);
            map.addViewInLayout(systemView);
            systemView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    selectSystem(systemView);
                }

            });
        }

        if (firstUpdate) {
            firstUpdate = false;

            Log.debug("Home system at " + homeSystem.location);
            mapScrollView.setScrollCenter(new Point((float) homeSystem.location.x * zoom, (float) homeSystem.location.y * zoom));
        }

    }

    private void selectSystem(SystemView systemView) {
        if (selection != null) {
            selection.setSelected(false);
        }
        selection = systemView;
        systemView.setSelected(true);

        if (selection == null) {
            selectedSystemPanel.setVisible(false);
        } else {
            selectedSystemTitle.setText(selection.getSystem().name);
            selectedSystemPanel.setVisible(true);
        }
    }

    private void zoom(Point point, float zoomFactor) {

        Point mousePoint = point.add(mapScrollView.getScrollOffset());

        Point staticPointBase = mousePoint.minus(mapScrollView.getScrollOffset()).divide(zoom);

        zoom *= zoomFactor;

        for (View child : map.getChildren()) {
            if (child instanceof SystemView) {
                SystemView systemView = (SystemView) child;
                systemView.setZoom(zoom);
            }
        }

        mapScrollView.setScrollOffset(mousePoint.minus(staticPointBase.multiply(zoom)));
    }

}
