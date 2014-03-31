package com.irr310.client.graphics.ether.activities.worldmap;

import java.util.List;

import org.newdawn.slick.util.Log;

import com.irr310.client.graphics.ether.activities.BoardActivity;
import com.irr310.client.graphics.ether.activities.systemmap.SystemMapActivity;
import com.irr310.client.graphics.ether.activities.systemmap.SystemMapActivity.SystemMapActivityBundle;
import com.irr310.client.navigation.LoginManager;
import com.irr310.common.tools.TransformMatrix;
import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;
import com.irr310.common.world.WorldMap;
import com.irr310.common.world.system.Ship;
import com.irr310.common.world.system.WorldSystem;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.SelectionManager;
import com.irr310.i3d.SelectionManager.OnSelectionChangeListener;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.LinearLayout;
import com.irr310.i3d.view.Point;
import com.irr310.i3d.view.RelativeLayout;
import com.irr310.i3d.view.ScrollView;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.i3d.view.View.OnMouseEventListener;
import com.irr310.server.Time.Timestamp;
import com.irr310.server.engine.system.SystemEngineObserver;
import com.irr310.server.engine.world.WorldEngine;
import com.irr310.server.engine.world.WorldEngineObserver;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;
import fr.def.iss.vd2.lib_v3d.V3DMouseEvent.Action;

public class WorldMapActivity extends Activity {

    protected WorldSystem selectedSystem;
    private RelativeLayout map;
    private ScrollView mapScrollView;
    private boolean firstUpdate = true;
    private float zoom;
    private TextView selectedSystemTitle;
    private LinearLayout selectedSystemPanel;
    private WorldEngine worldEngine;
    private Faction mFaction;
    private WorldMap worldMap;
    private SelectionManager<WorldSystem> systemViewSelectionManager;
    private Button inspectSystemButton;
    private static final int UPDATE_FACTION_WHAT = 1;
    private static final int UPDATE_MAP_WHAT = 2;
    private WorldEngineObserver mWorldEngineObserver;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/world_map");
        map = (RelativeLayout) findViewById("map@layout/world_map");
        mapScrollView = (ScrollView) findViewById("mapScrollView@layout/world_map");
        selectedSystemTitle = (TextView) findViewById("selectedSystemTitle@layout/world_map");
        selectedSystemPanel = (LinearLayout) findViewById("selectedSystemPanel@layout/world_map");
        inspectSystemButton = (Button) findViewById("inspectSystemButton@layout/world_map");

        zoom = 8f;

        worldEngine = (WorldEngine) bundle.getObject();
        worldMap = worldEngine.getWorld().getMap();
        mFaction = LoginManager.getLocalPlayer().getFaction();


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

        
        mWorldEngineObserver = new WorldEngineObserver() {
            
            @Override
            public void onStocksChanged(FactionStocks stocks) {
            }
            
            @Override
            public void onProductionChanged(FactionProduction production) {
            }
            
            @Override
            public void onPlayerConnected(Player player) {
            }
            
            @Override
            public void onFactionChanged(Faction faction) {
                    getHandler().removeMessages(UPDATE_FACTION_WHAT);
                    getHandler().obtainMessage(UPDATE_FACTION_WHAT).send();
            }
        };        
        
        systemViewSelectionManager = new SelectionManager<WorldSystem>();
        
        systemViewSelectionManager.addOnSelectionChangeListener(new OnSelectionChangeListener<WorldSystem>() {

            @Override
            public void onSelectionChange(List<WorldSystem> selection) {
                if(selection.size() == 1) {
                    selectedSystem = selection.get(0);

                    selectedSystemTitle.setText(selectedSystem.getName());
                    selectedSystemPanel.setVisible(true);
                } else {
                    selectedSystem = null;
                }
            }

            @Override
            public boolean mustClear(Object clearKey) {
                // TODO Auto-generated method stub
                return false;
            }});
        
        inspectSystemButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(V3DMouseEvent mouseEvent, View view) {
                if(selectedSystem != null) {
                    inspectSystemAction(selectedSystem);
                }
            }
        });
        
    }

    public void inspectSystemAction(WorldSystem selectedSystem) {
        SystemMapActivityBundle bundle = new SystemMapActivityBundle(selectedSystem);
        startActivity(new Intent(SystemMapActivity.class, bundle));
    }
    
    @Override
    public void onResume() {
        firstUpdate = true;
        worldEngine.getWorldEnginObservable().register(this, mWorldEngineObserver);
        getHandler().removeMessages(UPDATE_FACTION_WHAT);
        getHandler().obtainMessage(UPDATE_FACTION_WHAT).send();
    }

    @Override
    public void onPause() {
        worldEngine.getWorldEnginObservable().unregister(this);
        
    }

    @Override
    public void onDestroy() {
    }

    @Override
    protected void onMessage(Message message) {
         switch (message.what) {
            case UPDATE_FACTION_WHAT:
                updateMap();
                break;
        }
    }

    @Override
    protected void onUpdate(Timestamp time) {
        if(firstUpdate) {
            updateMap();
        }
    }
    
    private void updateMap() {

        map.removeAllView();
        
        WorldSystem homeSystem = mFaction.getHomeSystem();

        List<WorldSystem> allSystems = worldMap.getSystems();
        for (final WorldSystem system : allSystems) {
            final SystemView systemView = new SystemView(this, system, systemViewSelectionManager);
            systemView.setZoom(zoom);
            map.addViewInLayout(systemView);
        }

        if (firstUpdate) {
            firstUpdate = false;

            Log.debug("Home system at " + homeSystem.getLocation());
            mapScrollView.setScrollCenter(new Point((float) homeSystem.getLocation().x * zoom, (float) homeSystem.getLocation().y * zoom));
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
