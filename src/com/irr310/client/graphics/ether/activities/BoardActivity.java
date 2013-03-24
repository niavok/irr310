package com.irr310.client.graphics.ether.activities;

import com.irr310.client.graphics.ether.activities.production.ProductionActivity;
import com.irr310.client.graphics.ether.activities.worldmap.WorldMapActivity;
import com.irr310.client.navigation.LoginManager;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.FactionStateEvent;
import com.irr310.common.event.world.QueryFactionStateEvent;
import com.irr310.common.event.world.WorldEventDispatcher;
import com.irr310.common.event.world.WorldEventVisitor;
import com.irr310.common.world.view.FactionView;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Handler;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.server.Time;
import com.irr310.server.WorldEngine;

public class BoardActivity extends Activity {

    
    private WorldEventDispatcher worldEngine;
    private Button productionButton;
    private Button worldMapButton;
    private TextView boardStatersAmountTextView;
    private TextView boardOresAmountTextView;
    private TextView boardKoliumAmountTextView;
    private TextView boardNeuridiumAmountTextView;
    private WorldEventVisitor visitor;
    private Handler handler = new Handler();
    private static final int UPDATE_FACTION_WHAT = 1;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/board");
        worldEngine = ((BoardActivityBundle) bundle).getWorldEngine();
        
        
        worldMapButton = (Button) findViewById("seeWorldMapButton@layout/board");
        productionButton = (Button) findViewById("manageProductionButton@layout/board");
        boardStatersAmountTextView = (TextView) findViewById("boardStatersAmountTextView@layout/board");
        boardOresAmountTextView = (TextView) findViewById("boardOresAmountTextView@layout/board");
        boardKoliumAmountTextView = (TextView) findViewById("boardKoliumAmountTextView@layout/board");
        boardNeuridiumAmountTextView = (TextView) findViewById("boardNeuridiumAmountTextView@layout/board");
        
        
        worldMapButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle(worldEngine);
                startActivity(new Intent(WorldMapActivity.class, bundle));
            }
        });
        
        productionButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle(worldEngine);
                startActivity(new Intent(ProductionActivity.class, bundle));
            }
        });
        
        visitor = new DefaultWorldEventVisitor() {
            
            @Override
            public void visit(FactionStateEvent event) {
                if(LoginManager.getLocalPlayer().faction.id == event.getFaction().id) {
                    handler.obtainMessage(UPDATE_FACTION_WHAT, event.getFaction()).send();
                }
            }
        };
        
    }

    protected void updateFields(FactionView faction) {
        boardStatersAmountTextView.setText(faction.statersAmount+" [staters@icons]");
        boardOresAmountTextView.setText(faction.oresAmount+" [ores@icons]");
        boardKoliumAmountTextView.setText(faction.koliumAmount+" [kolium@icons]");
        boardNeuridiumAmountTextView.setText(faction.neuridiumAmount+" [neuridium@icons]");
    }

    @Override
    public void onResume() {
        worldEngine.registerEventVisitor(visitor);
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
        while(handler.hasMessages()) {
            Message message = handler.getMessage();
            
            switch(message.what) {
                case UPDATE_FACTION_WHAT:
                    updateFields((FactionView) message.obj);
                    break;
            }
            
        }
    }
    
    public static class BoardActivityBundle extends Bundle {

        public BoardActivityBundle(WorldEventDispatcher worldEngine) {
            super(worldEngine);
        }
        
        public WorldEventDispatcher getWorldEngine() {
            return (WorldEngine) getObject();
        }
    }

}
