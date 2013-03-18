package com.irr310.client.graphics.ether.activities;

import com.irr310.client.GameClient;
import com.irr310.client.graphics.ether.activities.BoardActivity.BoardActivityBundle;
import com.irr310.client.navigation.LoginManager;
import com.irr310.common.engine.EngineManager;
import com.irr310.common.event.game.GameEvent;
import com.irr310.common.event.game.GameEventVisitor;
import com.irr310.common.event.world.ConnectPlayerEvent;
import com.irr310.common.event.world.DefaultWorldEventVisitor;
import com.irr310.common.event.world.PlayerConnectedEvent;
import com.irr310.common.world.World;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Handler;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Message;
import com.irr310.i3d.view.Activity;
import com.irr310.server.Time;
import com.irr310.server.WorldEngine;

public class NewGameActivity extends Activity {


    private Handler handler;
    World world = null;
    private WorldEngine worldEngine = null;
    
    private static final int NEW_GAME_CREATED_WHAT = 1;
    private EngineManager<GameEventVisitor, GameEvent> engineManager;
    
    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/main");
        setStackable(false);
        
        engineManager = GameClient.getInstance().getEngineManager();
        
        handler = new Handler();
        new Thread() {
            

            public void run() {
                worldEngine = new WorldEngine(null);
                worldEngine.registerEventVisitor(new NewGameEventVisitor());
                engineManager.startAndWait(worldEngine);
                
                worldEngine.sendToAll(new ConnectPlayerEvent("fredb219", true));
            };
        }.start();
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
        
        while(handler.hasMessages()) {
            Message message = handler.getMessage();
            
            switch(message.what) {
                case NEW_GAME_CREATED_WHAT:
                    BoardActivityBundle bundle = new BoardActivityBundle(world);
                    startActivity(new Intent(BoardActivity.class, bundle));
                    break;
            }
            
        }
    }
    
    private final class NewGameEventVisitor extends DefaultWorldEventVisitor {
         @Override
        public void visit(PlayerConnectedEvent event) {
             world = worldEngine.getWorld();
             LoginManager.localPlayer = event.getPlayer();
             handler.obtainMessage(NEW_GAME_CREATED_WHAT).send();
        }   
    }

}
