package com.irr310.client.graphics.ether.activities;

import com.irr310.client.GameClient;
import com.irr310.client.navigation.LoginManager;
import com.irr310.common.engine.EngineManager;
import com.irr310.common.world.Faction;
import com.irr310.common.world.FactionProduction;
import com.irr310.common.world.FactionStocks;
import com.irr310.common.world.Player;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.server.engine.world.WorldEngine;
import com.irr310.server.engine.world.WorldEngineObserver;
import com.irr310.server.game.Game;

public class NewGameActivity extends Activity {

    private WorldEngine worldEngine = null;
    
    private static final int NEW_GAME_CREATED_WHAT = 1;
    private EngineManager engineManager;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/main");
        setStackable(false);
        
        engineManager = GameClient.getInstance().getEngineManager();
        
        Game game = GameClient.getInstance().getGameManager().createGame();
        
        GameClient.getInstance().getGameManager().setActiveGame(game);
        
        game.load();
        
        worldEngine = new WorldEngine(game.getWorld());
        engineManager.add(worldEngine);
        worldEngine.start();
        
        
        worldEngine.getWorldEnginObservable().register(this, new WorldEngineObserver() {
            
            @Override
            public void onStocksChanged(FactionStocks stocks) {
            }
            
            @Override
            public void onProductionChanged(FactionProduction production) {
            }
            
            @Override
            public void onPlayerConnected(Player player) {
                LoginManager.localPlayer = player;
                getHandler().obtainMessage(NEW_GAME_CREATED_WHAT).send();
            }
            
            @Override
            public void onFactionChanged(Faction faction) {
            }
        });
        
        worldEngine.connectPlayerAction("fredb219", true);
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }
    
    @Override
    public void onDestroy() {
        worldEngine.getWorldEnginObservable().unregister(this);
    }

    @Override
    protected void onMessage(Message message) {
        switch(message.what) {
            case NEW_GAME_CREATED_WHAT:
                startActivity(new Intent(BoardActivity.class));
                break;
        }
    }
}
