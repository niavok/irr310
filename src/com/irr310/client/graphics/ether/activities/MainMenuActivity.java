package com.irr310.client.graphics.ether.activities;

import com.irr310.client.GameClient;
import com.irr310.i3d.Bundle;
import com.irr310.i3d.Intent;
import com.irr310.i3d.Measure;
import com.irr310.i3d.Measure.Axis;
import com.irr310.i3d.input.I3dMouseEvent;
import com.irr310.i3d.view.Activity;
import com.irr310.i3d.view.Button;
import com.irr310.i3d.view.View;
import com.irr310.i3d.view.View.OnClickListener;
import com.irr310.server.Duration;
import com.irr310.server.GameServer;
import com.irr310.server.Time;
import com.irr310.server.Time.Timestamp;
import com.irr310.server.game.Game;

public class MainMenuActivity extends Activity {

    //private Triangle mobileLogoPart;
    private Measure animationMesure;
    private Time startTime;
    private Button newGameMenu;
    private Button continueGameButton;

    @Override
    public void onCreate(Bundle bundle) {
        setContentView("main@layout/mainmenu");

      //  mobileLogoPart = (Triangle) findViewById("logoRedPart@layout/logo");
        animationMesure = new Measure(0, true, Axis.VERTICAL);

        newGameMenu = (Button) findViewById("newGameButton@layout/mainmenu");
        continueGameButton = (Button) findViewById("continueGameButton@layout/mainmenu");
        
        newGameMenu.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                startActivity(new Intent(NewGameActivity.class));
            }
        });
        
        continueGameButton.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(I3dMouseEvent mouseEvent, View view) {
                startActivity(new Intent(ContinueGameActivity.class));
            }
        });
        
    }

    @Override
    public void onResume() {
        startTime = Time.now(false);

        // Pause the game in the main menu
        Time.pauseGame(startTime);
    }

    @Override
    public void onPause() {

    }
    
    @Override
    public void onDestroy() {
    }

    @Override
    protected void onUpdate(Timestamp time) {

        if(GameClient.getInstance().getGameManager().hasPreviousGame()) {
            continueGameButton.setEnabled(true);
        } else {
            continueGameButton.setEnabled(false);
        }
    }

}
