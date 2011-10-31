package com.irr310.client;

import javax.swing.UIManager;

import com.irr310.server.ParameterAnalyser;

public class Irr310Client {

	private static LoginForm loginForm;

	public static void main(String[] args) {
	    setLookAndFeel();
	    
		// Init static game client
	    ParameterAnalyser parameterAnalyser = new ParameterAnalyser(args);
	    GameClient gameClient = new GameClient(parameterAnalyser);
	    gameClient.run();
		
		
		

		
	}

	private static void setLookAndFeel() {
		for (UIManager.LookAndFeelInfo laf : UIManager
				.getInstalledLookAndFeels()) {
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
