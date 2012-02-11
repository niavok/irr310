package com.irr310.client;

import javax.swing.UIManager;

import com.irr310.common.tools.Log;
import com.irr310.server.ParameterAnalyser;

public class Irr310Client {

    private static LoginForm loginForm;

    public static void main(String[] args) {
        Log.perfBegin("Init");

        Log.perfBegin("Set LookAndFeel");
        setLookAndFeel();
        Log.perfEnd(); // SetLookAndFeel

        // Init static game client
        Log.perfBegin("Init ParameterAnalyser");
        ParameterAnalyser parameterAnalyser = new ParameterAnalyser(args);
        Log.perfEnd(); // ParameterAnalyser
        
        Log.perfBegin("Init ParameterAnalyser");
        GameClient gameClient = new GameClient(parameterAnalyser);
        Log.perfEnd(); // GameClient
        
        Log.perfEnd();
        gameClient.run();

    }

    private static void setLookAndFeel() {
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(laf.getName())) {
                try {
                    UIManager.setLookAndFeel(laf.getClassName());

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }
    }

}
