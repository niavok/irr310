package com.irr310.client.graphics.ether.activities;

import com.irr310.client.graphics.ether.activities.production.FactoryActivity;
import com.irr310.client.graphics.ether.activities.worldmap.WorldMapActivity;
import com.irr310.client.navigation.LoginManager;
import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.TextView;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.server.GameServer;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;
import com.irr310.server.engine.world.WorldEngine;
import com.irr310.server.engine.world.WorldEngineObserver;

import fr.def.iss.vd2.lib_v3d.V3DMouseEvent;

public class BoardActivity extends Activity {

    
    private WorldEngine worldEngine;
    private Button productionButton;
    private Button worldMapButton;
    private TextView boardStatersAmountTextView;
    private TextView boardOresAmountTextView;
    private TextView boardKoliumAmountTextView;
    private TextView boardNeuridiumAmountTextView;
    private Faction mFaction;
    private WorldEngineObserver mWorldEngineObserver;
    private static final int UPDATE_FACTION_WHAT = 1;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/board");
        worldEngine = GameServer.getWorldEngine();
        
        mFaction = LoginManager.getLocalPlayer().getFaction();
        
        worldMapButton = (Button) findViewById("seeWorldMapButton@layout/board");
        productionButton = (Button) findViewById("manageProductionButton@layout/board");
        boardStatersAmountTextView = (TextView) findViewById("boardStatersAmountTextView@layout/board");
        boardOresAmountTextView = (TextView) findViewById("boardOresAmountTextView@layout/board");
        boardKoliumAmountTextView = (TextView) findViewById("boardKoliumAmountTextView@layout/board");
        boardNeuridiumAmountTextView = (TextView) findViewById("boardNeuridiumAmountTextView@layout/board");
        
        
        worldMapButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(V3DMouseEvent mouseEvent, View view) {
                Bundle bundle = new Bundle(worldEngine);
                startActivity(new Intent(WorldMapActivity.class, bundle));
            }
        });
        
        productionButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(V3DMouseEvent mouseEvent, View view) {
                Bundle bundle = new Bundle(worldEngine);
                startActivity(new Intent(FactoryActivity.class, bundle));
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
                if (mFaction.equals(faction)) {
                    getHandler().removeMessages(UPDATE_FACTION_WHAT);
                    getHandler().obtainMessage(UPDATE_FACTION_WHAT, faction).send();
                }
            }
        }; 
    }

    protected void updateFields() {
        boardStatersAmountTextView.setText(mFaction.getStatersAmount()+" [staters@icons]");
        boardOresAmountTextView.setText(mFaction.getOresAmount()+" [ores@icons]");
        boardKoliumAmountTextView.setText(mFaction.getKoliumAmount()+" [kolium@icons]");
        boardNeuridiumAmountTextView.setText(mFaction.getNeuridiumAmount()+" [neuridium@icons]");
    }

    @Override
    public void onResume() {
        worldEngine.getWorldEnginObservable().register(this, mWorldEngineObserver);
        updateFields();
    }

    @Override
    public void onPause() {
        worldEngine.getWorldEnginObservable().unregister(this);
    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Timestamp time) {
    }
    
    @Override
    protected void onMessage(Message message) {
        switch(message.what) {
            case UPDATE_FACTION_WHAT:
                updateFields();
                break;
        }
    }

}
